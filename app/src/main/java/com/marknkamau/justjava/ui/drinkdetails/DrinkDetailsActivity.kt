package com.marknkamau.justjava.ui.drinkdetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.CartRepositoryImpl
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.CoffeeDrink
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.CatalogAdapter
import com.marknkamau.justjava.ui.profile.ProfileActivity
import com.squareup.picasso.Picasso

import com.marknkamau.justjava.utils.bindView

class DrinkDetailsActivity : AppCompatActivity(), DrinkDetailsView, View.OnClickListener {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val imgDrinkImage: ImageView by bindView(R.id.img_drink_image)
    val tvDrinkName: TextView by bindView(R.id.tv_drink_name)
    val tvDrinkDescription: TextView by bindView(R.id.tv_drink_description)
    val tvDrinkContents: TextView by bindView(R.id.tv_drink_contents)
    val tvDrinkPrice: TextView by bindView(R.id.tv_drink_price)
    val imgMinusQty: ImageView by bindView(R.id.img_minus_qty)
    val tvQuantity: TextView by bindView(R.id.tv_quantity)
    val imgAddQty: ImageView by bindView(R.id.img_add_qty)
    val tvSubtotal: TextView by bindView(R.id.tv_subtotal)
    val btnAddToCart: Button by bindView(R.id.btn_add_to_cart)
    val cbCinnamon: CheckBox by bindView(R.id.cb_cinnamon)
    val cbChocolate: CheckBox by bindView(R.id.cb_chocolate)
    val cbMarshmallow: CheckBox by bindView(R.id.cb_marshmallow)

    private lateinit var drink: CoffeeDrink
    private var quantity: Int = 0
    private lateinit var presenter: DrinkDetailsPresenter
    private var isSignedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val authenticationService = (application as JustJavaApp).authService

        presenter = DrinkDetailsPresenter(this, preferencesRepository, authenticationService, CartRepositoryImpl)

        drink = intent.extras.getParcelable<CoffeeDrink>(CatalogAdapter.DRINK_KEY)

        tvDrinkName.text = drink.drinkName
        tvDrinkContents.text = drink.drinkContents
        tvDrinkDescription.text = drink.drinkDescription
        tvDrinkPrice.text = String.format("%s%s", resources.getString(R.string.ksh), drink.drinkPrice)
        tvSubtotal.text = String.format("%s%s", resources.getString(R.string.ksh), drink.drinkPrice)

        val drinkImage = "file:///android_asset/" + drink.drinkImage
        val picasso = Picasso.with(this)
        picasso.load(drinkImage).noFade().into(imgDrinkImage)

        quantity = 1

        imgMinusQty.setOnClickListener(this)
        imgAddQty.setOnClickListener(this)
        btnAddToCart.setOnClickListener(this)
        cbChocolate.setOnClickListener(this)
        cbCinnamon.setOnClickListener(this)
        cbMarshmallow.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getSignInStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (isSignedIn) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu)
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this@DrinkDetailsActivity, CartActivity::class.java))
                return true
            }
            R.id.menu_log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
                return true
            }
            R.id.menu_log_out -> {
                presenter.logUserOut()
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

    override fun onClick(view: View) {
        when (view) {
            imgMinusQty -> minusQty()
            imgAddQty -> addQty()
            btnAddToCart -> addToCart()
            cbCinnamon -> updateSubtotal()
            cbChocolate -> updateSubtotal()
            cbMarshmallow -> updateSubtotal()
        }
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() {
        finish()
    }

    override fun setSignInStatus(status: Boolean) {
        isSignedIn = status
        invalidateOptionsMenu()
    }

    private fun addToCart() {
        val cinnamon = if (cbCinnamon.isChecked) "true" else "false"
        val choc = if (cbChocolate.isChecked) "true" else "false"
        val marshmallow = if (cbMarshmallow.isChecked) "true" else "false"
        val total = updateSubtotal()

        val item = CartItem(
                0, drink.drinkName, quantity.toString(), cinnamon, choc, marshmallow, total
        )
        presenter.addToCart(item)
    }

    private fun minusQty() {
        if (quantity > 1) {
            quantity -= 1
        }
        tvQuantity.text = quantity.toString()
        updateSubtotal()
    }

    private fun addQty() {
        quantity += 1
        tvQuantity.text = quantity.toString()
        updateSubtotal()
    }

    private fun updateSubtotal(): Int {
        var base = Integer.parseInt(drink.drinkPrice)
        base *= quantity
        if (cbCinnamon.isChecked) {
            base += quantity * 100
        }
        if (cbChocolate.isChecked) {
            base += quantity * 100
        }
        if (cbMarshmallow.isChecked) {
            base += quantity * 100
        }
        tvSubtotal.text = resources.getString(R.string.ksh) + base
        return base
    }
}
