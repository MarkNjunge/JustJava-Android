package com.marknkamau.justjava.ui.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom
import com.marknkamau.justjava.ui.BaseActivity

import com.marknkamau.justjava.utils.bindView
import io.reactivex.android.schedulers.AndroidSchedulers

class CartActivity : BaseActivity(), CartView, View.OnClickListener {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val btnClearCart: Button by bindView(R.id.btn_clear_cart)
    val tvNoItems: TextView by bindView(R.id.tv_no_items)
    val rvCart: RecyclerView by bindView(R.id.rv_cart)
    val tvCartTotal: TextView by bindView(R.id.tv_cart_total)
    val btnCheckout: Button by bindView(R.id.btn_checkout)

    private lateinit var cartDao: CartDao
    private lateinit var presenter: CartPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        rvCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        cartDao = (application as JustJavaApp).cartDatabase.cartDao()
        presenter = CartPresenter(this, cartDao)
        presenter.loadItems()

        btnClearCart.setOnClickListener(this)
        btnCheckout.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    override fun onClick(view: View) {
        when (view) {
            btnClearCart -> presenter.clearCart()
            btnCheckout -> startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
        }
    }

    override fun displayCart(cartItems: MutableList<CartItemRoom>?) {
        val adapter = CartAdapter(this, cartItems, object : CartAdapter.CartAdapterListener {
            override fun updateList() {
                presenter.loadItems()
            }
        }, cartDao)

        rvCart.adapter = adapter
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
