package com.marknkamau.justjava.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import kotlinx.android.synthetic.main.activity_cart.*
import org.koin.android.ext.android.inject

class CartActivity : BaseActivity(), CartView {
    private lateinit var presenter: CartPresenter
    private lateinit var adapter: CartAdapter
    private val cartDao: CartDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        presenter = CartPresenter(this, cartDao)
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

        adapter = CartAdapter(this) { cartItem ->
            editCartDialog.arguments = Bundle().apply {
                putParcelable(EditCartDialog.CART_ITEM, cartItem)
            }
            editCartDialog.show(supportFragmentManager, "edit_cart_dialog")
        }
        rvCart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCart.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvCart.adapter = adapter

        btnClearCart.setOnClickListener {
            presenter.clearCart()
        }

        btnCheckout.setOnClickListener {
            startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    override fun displayCart(orderItems: MutableList<CartItem>) {
        tvNoItems.visibility = View.GONE
        adapter.setItems(orderItems)
        btnCheckout.isEnabled = true
    }

    override fun displayCartTotal(totalCost: Int) {
        tvCartTotal.text = getString(R.string.price_listing, totalCost)
    }

    override fun displayEmptyCart() {
        tvNoItems.visibility = View.VISIBLE
        groupCartNotEmpty.visibility = View.GONE
    }

    override fun displayMessage(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
