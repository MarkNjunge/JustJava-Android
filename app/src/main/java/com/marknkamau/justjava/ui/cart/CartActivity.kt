package com.marknkamau.justjava.ui.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.data.CartRepositoryImpl
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import com.marknkamau.justjava.models.CartItem

import com.marknkamau.justjava.utils.bindView

class CartActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener, CartView, View.OnClickListener {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val btnClearCart: Button by bindView(R.id.btn_clear_cart)
    val tvNoItems: TextView by bindView(R.id.tv_no_items)
    val rvCart: RecyclerView by bindView(R.id.rv_cart)
    val tvCartTotal: TextView by bindView(R.id.tv_cart_total)
    val btnCheckout: Button by bindView(R.id.btn_checkout)

    lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var presenter: CartPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        firebaseAuth = FirebaseAuth.getInstance()

        rvCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val preferencesRepository: PreferencesRepository = (application as JustJavaApp).preferencesRepo
        presenter = CartPresenter(this, preferencesRepository, CartRepositoryImpl)
        presenter.loadItems()

        btnClearCart.setOnClickListener(this)
        btnCheckout.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        user = firebaseAuth.currentUser
        val inflater = menuInflater
        if (user != null) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu)
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
                return true
            }
            R.id.menu_log_out -> {
                presenter.logUserOut()
                invalidateOptionsMenu()
                return true
            }
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        user = FirebaseAuth.getInstance().currentUser
        invalidateOptionsMenu()
    }

    override fun onClick(view: View) {
        when (view) {
            btnClearCart -> presenter.clearCart()
            btnCheckout -> startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
        }
    }

    override fun displayCart(cartItems: MutableList<CartItem>) {
        val adapter = CartAdapter(this, cartItems, object : CartAdapter.CartAdapterListener {
            override fun updateList() {
                presenter.loadItems()
            }
        })

        rvCart.adapter = adapter
        btnCheckout.isEnabled = true
    }

    override fun displayCartTotal(totalCost: Int) {
        tvCartTotal.text = getString(R.string.total) + ": " + getString(R.string.ksh) + totalCost
    }

    override fun displayEmptyCart() {
        tvNoItems.visibility = View.VISIBLE
        btnClearCart.isEnabled = false
        btnClearCart.alpha = .54f
        tvCartTotal.alpha = .54f
        tvCartTotal.text = getString(R.string.total) + ": " + getString(R.string.ksh)
        btnCheckout.setBackgroundResource(R.drawable.large_button_disabled)
        btnCheckout.isEnabled = false
    }


}
