package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.ui.viewOrder.ViewOrderActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.DividerItemDecorator
import com.marknkamau.justjava.utils.onTextChanged
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_order_item.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CartActivity : BaseActivity(), CartView {

    private val presenter: CartPresenter by inject { parametersOf(this) }
    private lateinit var adapter: BaseRecyclerViewAdapter<CartItem>
    private var payMpesa = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.cart)

        presenter.loadItems()

        val editCartDialog = EditCartDialog().apply {
            onComplete = { editType, cartItem ->
                dismiss()
                if (editType == EditCartDialog.EditType.DELETE) {
                    presenter.deleteItem(cartItem)
                } else {
                    presenter.updateItem(cartItem)
                }
            }
        }

        adapter = BaseRecyclerViewAdapter(R.layout.item_order_item) { cartItem ->
            tvItemNameItem.text = cartItem.itemName
            tvItemQtyItem.text = "${cartItem.itemQty}x"
            tvItemPriceItem.text = context.getString(R.string.price_listing, cartItem.itemPrice)

            val toppings = mutableListOf<String>()

            if (cartItem.itemCinnamon) toppings.add("Cinnamon")
            if (cartItem.itemChoc) toppings.add("Chocolate")
            if (cartItem.itemMarshmallow) toppings.add("Marshmallows")

            if (toppings.isNotEmpty()) {
                tvToppingsItem.visibility = View.VISIBLE
                tvToppingsItem.text = toppings.joinToString(", ")
            } else {
                tvToppingsItem.visibility = View.GONE
            }
            orderItemRootLayout.setOnClickListener {
                editCartDialog.arguments = Bundle().apply {
                    putParcelable(EditCartDialog.CART_ITEM, cartItem)
                }
                editCartDialog.show(supportFragmentManager, "edit_cart_dialog")
            }
        }

        rvCart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecorator = DividerItemDecorator(getDrawable(R.drawable.custom_item_divider)!!)
        rvCart.addItemDecoration(dividerItemDecorator)
        rvCart.adapter = adapter

        tvPaymentMethodCart.text = "M-Pesa"

        etAddressCart.onTextChanged { tilAddressCart.error = null }

        btnClearCart.setOnClickListener {
            presenter.clearCart()
        }

        btnChangePaymentMethodCart.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(R.string.payment_method)
                    .setItems(arrayOf("M-Pesa", "Cash on Delivery")) { _, which ->
                        if (which == 0) {
                            payMpesa = true
                            tvPaymentMethodCart.text = "M-Pesa"
                        } else {
                            payMpesa = false
                            tvPaymentMethodCart.text = "Cash on Delivery"
                        }
                    }
                    .create()
                    .show()

        }

        btnLoginCart.setOnClickListener { startActivity(Intent(this, LogInActivity::class.java)) }
        btnSignUpCart.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }

        btnPlaceOrder.setOnClickListener {
            if (etAddressCart.trimmedText.isEmpty()) {
                etAddressCart.error = getString(R.string.required)
                return@setOnClickListener
            }
            presenter.placeOrder(etAddressCart.trimmedText, etCommentsCart.trimmedText, payMpesa)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getSignInStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun onLoggedOut() {
        setDisplayToLoggedOut()
    }

    override fun setDisplayToLoggedIn(userDetails: UserDetails) {
        TransitionManager.beginDelayedTransition(cartActivityRootLayout)
        layoutCartSignedIn.visibility = View.VISIBLE
        layoutCartSignedOut.visibility = View.GONE
        etAddressCart.setText(userDetails.address)
    }

    override fun setDisplayToLoggedOut() {
        TransitionManager.beginDelayedTransition(cartActivityRootLayout)
        layoutCartSignedIn.visibility = View.GONE
        layoutCartSignedOut.visibility = View.VISIBLE
    }

    override fun displayCart(orderItems: MutableList<CartItem>) {
        TransitionManager.beginDelayedTransition(cartActivityRootLayout)
        layoutCartContent.visibility = View.VISIBLE
        layoutCartEmpty.visibility = View.GONE
        adapter.setItems(orderItems)
    }

    override fun displayEmptyCart() {
        TransitionManager.beginDelayedTransition(cartActivityRootLayout)
        layoutCartContent.visibility = View.GONE
        layoutCartEmpty.visibility = View.VISIBLE
    }

    override fun displayCartTotal(total: Int) {
        tvTotalCart.text = getString(R.string.price_listing, total)
    }

    override fun showLoadingBar() {
        btnPlaceOrder.isEnabled = false
        pbLoadingCart.visibility = View.VISIBLE
    }

    override fun hideLoadingBar() {
        btnPlaceOrder.isEnabled = true
        pbLoadingCart.visibility = View.GONE
    }

    override fun finishActivity(order: Order) {
        val i = Intent(this, ViewOrderActivity::class.java)
        i.putExtra(ViewOrderActivity.ORDER_KEY, order)

        TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(Intent(this, MainActivity::class.java))
                .addNextIntentWithParentStack(i)
                .startActivities()

        finish()
    }

    override fun displayMessage(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
