package com.marknkamau.justjava.ui.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.TaskStackBuilder
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.PaymentMethod
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivityCheckoutBinding
import com.marknkamau.justjava.ui.addAddress.AddAddressActivity
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.orderDetail.OrderDetailActivity
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.capitalize
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CheckoutActivity : BaseActivity() {

    private val checkoutViewModel: CheckoutViewModel by viewModel()
    private lateinit var paymentMethod: PaymentMethod
    private var deliveryAddress: Address? = null
    private lateinit var user: User
    override var requiresSignedIn = true
    private lateinit var binding: ActivityCheckoutBinding

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Checkout"

        // Should already be checked before launching this activity
        if (!checkoutViewModel.isSignedIn()) {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Set mpesa as the default payment method
        paymentMethod = PaymentMethod.MPESA
        binding.tvPaymentMethod.text = paymentMethod.s.capitalize()

        observeCartItems()
        observeLoading()

        checkoutViewModel.getCartItems()
        loadAddressList()

        binding.btnChangeDeliveryAddress.setOnClickListener {
            showChangeDeliveryAddressDialog()
        }
        binding.btnChangePaymentMethod.setOnClickListener {
            showChangePaymentMethodDialog()
        }

        binding.btnPlaceOrder.setOnClickListener {
            if (valid()) {
                placeOrder()
            }
        }
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val address = result.data?.extras!![AddAddressActivity.ADDRESS_KEY] as Address

                    checkoutViewModel.addAddress(address).observe(this, { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                binding.btnAddDeliveryAddress.visibility = View.GONE
                                binding.btnChangeDeliveryAddress.visibility = View.VISIBLE
                                binding.tvDeliveryAddress.visibility = View.VISIBLE
                                binding.btnPlaceOrder.isEnabled = true

                                loadAddressList()
                            }
                            is Resource.Failure -> handleApiError(resource)
                        }
                    })
                }
            }
        binding.btnAddDeliveryAddress.setOnClickListener {
            resultLauncher.launch(Intent(this, AddAddressActivity::class.java))
        }
    }

    private fun observeLoading() {
        checkoutViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeCartItems() {
        checkoutViewModel.items.observe(this, { items ->
            if (items.isEmpty()) finish()

            binding.tvCartItems.text = items.joinToString(", ") { it.cartItem.productName }
            binding.tvCartItemsCount.text = resources.getQuantityString(R.plurals.item_s, items.size, items.size)
            val total = items.fold(0.0, { acc, c -> acc + c.cartItem.totalPrice })
            binding.tvCartTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(total))
        })
    }

    private fun loadAddressList() {
        user = checkoutViewModel.getUser()
        if (user.address.isEmpty()) {
            binding.btnAddDeliveryAddress.visibility = View.VISIBLE
            binding.btnChangeDeliveryAddress.visibility = View.GONE
            binding.tvDeliveryAddress.visibility = View.GONE
            binding.btnPlaceOrder.isEnabled = false
        } else {
            binding.btnAddDeliveryAddress.visibility = View.GONE
            binding.btnChangeDeliveryAddress.visibility = View.VISIBLE
            binding.tvDeliveryAddress.visibility = View.VISIBLE

            deliveryAddress = user.address[0]
            binding.tvDeliveryAddress.text = deliveryAddress!!.streetAddress
        }
    }

    private fun showChangeDeliveryAddressDialog() {
        val addresses = user.address.map { it.streetAddress }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(R.string.delivery_address)
            .setItems(addresses) { _, which ->
                deliveryAddress = user.address[which]
                binding.tvDeliveryAddress.text = deliveryAddress!!.streetAddress
            }
            .create()
            .show()
    }

    @SuppressLint("DefaultLocale")
    private fun showChangePaymentMethodDialog() {
        val paymentMethods = PaymentMethod.values().map { it.s.capitalize() }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(R.string.payment_method)
            .setItems(paymentMethods) { _, which ->
                paymentMethod = PaymentMethod.values()[which]
                binding.tvPaymentMethod.text = paymentMethod.s.capitalize()
            }
            .create()
            .show()
    }

    private fun placeOrder() {
        val additionalComments = if (binding.etAdditionalComments.trimmedText.isNotEmpty()) {
            binding.etAdditionalComments.trimmedText
        } else null

        binding.btnPlaceOrder.isEnabled = false
        checkoutViewModel.placeOrder(paymentMethod, deliveryAddress!!, additionalComments)
            .observe(this, { resource ->
                binding.btnPlaceOrder.isEnabled = true
                when (resource) {
                    is Resource.Success -> {
                        checkoutViewModel.clearCart()
                        toast("Order placed")
                        val intent = Intent(this, OrderDetailActivity::class.java).apply {
                            putExtra(OrderDetailActivity.ORDER_ID_KEY, resource.data.id)
                        }
                        TaskStackBuilder.create(this@CheckoutActivity)
                            .addNextIntentWithParentStack(Intent(this, MainActivity::class.java))
                            .addNextIntentWithParentStack(intent)
                            .startActivities()
                    }
                    is Resource.Failure -> handleApiError(resource)
                }
            })
    }

    private fun valid(): Boolean {
        var isValid = true

        if (deliveryAddress == null) {
            toast("A delivery address is required", Toast.LENGTH_LONG)
            isValid = false
        }

        return isValid
    }
}
