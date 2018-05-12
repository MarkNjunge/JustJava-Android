package com.marknkamau.justjava.ui.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : BaseActivity(), CartView {
    private lateinit var presenter: CartPresenter
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val cartDao = (application as JustJavaApp).cartDatabase.cartDao()

        presenter = CartPresenter(this, cartDao)
        presenter.loadItems()

        val editCartDialog = EditCartDialog()
        editCartDialog.cartDao = cartDao
        editCartDialog.onComplete = { editType, cartItem ->
            editCartDialog.dismiss()
            if (editType == EditCartDialog.EditType.DELETE) {
                presenter.deleteItem(cartItem)
            } else {
                presenter.updateItem(cartItem)
            }
        }

        adapter = CartAdapter(this, { cartItem ->
            val args = Bundle()
            args.putParcelable(EditCartDialog.CART_ITEM, cartItem)
            editCartDialog.arguments = args
            editCartDialog.show(supportFragmentManager, "edit_cart_dialog")
        })
        rvCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCart.adapter = adapter

        btnClearCart.setOnClickListener{
            presenter.clearCart()
        }

        btnCheckout.setOnClickListener{
            startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    override fun displayCart(orderItems: MutableList<OrderItem>) {
        adapter.setItems(orderItems)
        btnCheckout.isEnabled = true
    }

    override fun displayCartTotal(totalCost: Int) {
        tvCartTotal.text = getString(R.string.total) + ": " + getString(R.string.ksh) + totalCost
    }

    override fun displayEmptyCart() {
        rvCart.visibility = View.INVISIBLE
        tvNoItems.visibility = View.VISIBLE
        btnClearCart.isEnabled = false
        btnClearCart.alpha = .54f
        tvCartTotal.alpha = .54f
        tvCartTotal.text = getString(R.string.total) + ": " + getString(R.string.ksh)
        btnCheckout.setBackgroundResource(R.drawable.large_button_disabled)
        btnCheckout.isEnabled = false
    }

    override fun displayMessage(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
