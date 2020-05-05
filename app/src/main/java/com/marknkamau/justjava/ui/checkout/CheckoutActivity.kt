package com.marknkamau.justjava.ui.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.PaymentMethod
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.addAddress.AddAddressActivity
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.orderDetail.OrderDetailActivity
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_checkout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : BaseActivity() {

    companion object {
        private const val ADD_ADDRESS_REQ = 99
    }

    private val checkoutViewModel: CheckoutViewModel by viewModel()
    private lateinit var paymentMethod: PaymentMethod
    private var deliveryAddress: Address? = null
    private lateinit var user: User
    override var requiresSignedIn = true

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        supportActionBar?.title = "Checkout"

        // Should already be checked before launching this activity
        if (!checkoutViewModel.isSignedIn()) {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Set mpesa as the default payment method
        paymentMethod = PaymentMethod.MPESA
        tvPaymentMethod.text = paymentMethod.s.toLowerCase().capitalize()

        observeCartItems()
        observeLoading()

        checkoutViewModel.getCartItems()
        loadAddressList()

        btnChangeDeliveryAddress.setOnClickListener {
            showChangeDeliveryAddressDialog()
        }
        btnChangePaymentMethod.setOnClickListener {
            showChangePaymentMethodDialog()
        }

        btnPlaceOrder.setOnClickListener {
            if (valid()) {
                placeOrder()
            }
        }
        btnAddDeliveryAddress.setOnClickListener {
            startActivityForResult(Intent(this, AddAddressActivity::class.java), ADD_ADDRESS_REQ)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ADDRESS_REQ && resultCode == Activity.RESULT_OK) {
            val address = data?.extras!![AddAddressActivity.ADDRESS_KEY] as Address

            checkoutViewModel.addAddress(address).observe(this, Observer { resource ->
                when (resource) {
                    is Resource.Success -> {
                        btnAddDeliveryAddress.visibility = View.GONE
                        btnChangeDeliveryAddress.visibility = View.VISIBLE
                        tvDeliveryAddress.visibility = View.VISIBLE
                        btnPlaceOrder.isEnabled = true

                        loadAddressList()
                    }
                    is Resource.Failure -> handleApiError(resource)
                }
            })
        }
    }

    private fun observeLoading() {
        checkoutViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeCartItems() {
        checkoutViewModel.items.observe(this, Observer { items ->
            if (items.isEmpty()) finish()

            tvCartItems.text = items.joinToString(", ") { it.cartItem.productName }
            tvCartItemsCount.text = resources.getQuantityString(R.plurals.item_s, items.size, items.size)
            val total = items.fold(0.0, { acc, c -> acc + c.cartItem.totalPrice })
            tvCartTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(total))
        })
    }

    private fun loadAddressList() {
        user = checkoutViewModel.getUser()
        if (user.address.isEmpty()) {
            btnAddDeliveryAddress.visibility = View.VISIBLE
            btnChangeDeliveryAddress.visibility = View.GONE
            tvDeliveryAddress.visibility = View.GONE
            btnPlaceOrder.isEnabled = false
        } else {
            btnAddDeliveryAddress.visibility = View.GONE
            btnChangeDeliveryAddress.visibility = View.VISIBLE
            tvDeliveryAddress.visibility = View.VISIBLE

            deliveryAddress = user.address[0]
            tvDeliveryAddress.text = deliveryAddress!!.streetAddress
        }
    }

    private fun showChangeDeliveryAddressDialog() {
        val addresses = user.address.map { it.streetAddress }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(R.string.delivery_address)
            .setItems(addresses) { _, which ->
                deliveryAddress = user.address[which]
                tvDeliveryAddress.text = deliveryAddress!!.streetAddress
            }
            .create()
            .show()
    }

    @SuppressLint("DefaultLocale")
    private fun showChangePaymentMethodDialog() {
        val paymentMethods = PaymentMethod.values().map { it.s.toLowerCase().capitalize() }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(R.string.payment_method)
            .setItems(paymentMethods) { _, which ->
                paymentMethod = PaymentMethod.values()[which]
                tvPaymentMethod.text = paymentMethod.s.toLowerCase().capitalize()
            }
            .create()
            .show()
    }

    private fun placeOrder() {
        val additionalComments =
            if (etAdditionalComments.trimmedText.isNotEmpty()) etAdditionalComments.trimmedText else null

        btnPlaceOrder.isEnabled = false
        checkoutViewModel.placeOrder(paymentMethod, deliveryAddress!!, additionalComments)
            .observe(this, Observer { resource ->
                btnPlaceOrder.isEnabled = true
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
