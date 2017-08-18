package com.marknkamau.justjava.ui.drinkdetails

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.marknkamau.justjava.JustJavaApp

import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.CartItemRoom
import com.marknkamau.justjava.models.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.main.CatalogAdapter
import com.squareup.picasso.Picasso

import com.marknkamau.justjava.utils.bindView

class DrinkDetailsActivity : BaseActivity(), DrinkDetailsView, View.OnClickListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cartDao = (application as JustJavaApp).cartDatabase.cartDao()

        presenter = DrinkDetailsPresenter(this, cartDao)

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

    private fun addToCart() {
        val itemRm = CartItemRoom(0,
                drink.drinkName,
                quantity,
                cbCinnamon.isChecked,
                cbChocolate.isChecked,
                cbMarshmallow.isChecked,
                updateSubtotal()
                )

        presenter.addToCart(itemRm)
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
