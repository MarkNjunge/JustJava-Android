package com.marknkamau.justjava.ui.drinkdetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.marknkamau.justjava.R
import com.marknjunge.core.model.CoffeeDrink
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BaseActivity
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_drink_details.*
import kotlinx.android.synthetic.main.content_drink_details.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DrinkDetailsActivity : AppCompatActivity(), DrinkDetailsView, View.OnClickListener {

    companion object {
        const val DRINK_KEY = "drink_key"
    }

    private lateinit var drink: CoffeeDrink
    private var quantity: Int = 0
    private val presenter: DrinkDetailsPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_details)

        drink = intent.extras!!.getParcelable(DRINK_KEY) as CoffeeDrink

        tvDrinkName.text = drink.drinkName
        tvDrinkContents.text = drink.drinkContents
        tvDrinkDescription.text = drink.drinkDescription
        tvDrinkPrice.text = resources.getString(R.string.price_listing, drink.drinkPrice.toInt())
        tvSubtotal.text = resources.getString(R.string.price_listing, drink.drinkPrice.toInt())

        val drinkImage = "file:///android_asset/" + drink.drinkImage
        Picasso.get().load(drinkImage).noFade().into(imgDrinkImage)

        quantity = 1

        imgMinusQty.setOnClickListener(this)
        imgAddQty.setOnClickListener(this)
        btnAddToCart.setOnClickListener(this)
        cbChocolate.setOnClickListener(this)
        cbCinnamon.setOnClickListener(this)
        cbMarshmallow.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
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

    override fun displayMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() {
        finish()
    }

    private fun addToCart() {
        val itemRm = CartItem(0,
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
        tvSubtotal.text = resources.getString(R.string.price_listing, base)
        return base
    }
}
