package com.marknkamau.justjava.ui.drinkdetails

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.marknjunge.core.data.local.DrinksProvider
import com.marknjunge.core.model.CoffeeDrink
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_drink_details.*
import kotlinx.android.synthetic.main.content_drink_details.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DrinkDetailsActivity : AppCompatActivity(), DrinkDetailsView {

    companion object {
        private const val DRINK_KEY = "drink_key"

        fun start(context: Context, drink: CoffeeDrink, vararg sharedElements: Pair<View, String>) {
            val intent = Intent(context, DrinkDetailsActivity::class.java)
            intent.putExtra(DRINK_KEY, drink)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *sharedElements)

            context.startActivity(intent, options.toBundle())
        }
    }

    private lateinit var drink: CoffeeDrink
    private var quantity: Int = 0
    private val presenter: DrinkDetailsPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_details)

        drink = intent.extras!!.getParcelable(DRINK_KEY) as CoffeeDrink

        quantity = 1

        tvDrinkNameDetail.text = drink.drinkName
        tvDrinkContentsDetail.text = drink.drinkContents
        tvDrinkDescriptionDetail.text = drink.drinkDescription
        tvDrinkPriceDetail.text = resources.getString(R.string.price_listing, drink.drinkPrice.toInt())
        tvSubtotalDetail.text = resources.getString(R.string.price_listing, drink.drinkPrice.toInt())
        tvQuantityDetail.text = quantity.toString()
        val drinkImage = "file:///android_asset/" + drink.drinkImage
        Picasso.get().load(drinkImage).noFade().into(imgDrinkImageDetail)

        imgBackDetail.setOnClickListener { finish() }
        imgMinusQtyDetail.setOnClickListener { minusQty() }
        imgAddQtyDetail.setOnClickListener { addQty() }
        btnAddToCart.setOnClickListener { addToCart() }
        cbChocolateDetail.setOnClickListener { updateSubtotal() }
        cbCinnamonDetail.setOnClickListener { updateSubtotal() }
        cbMarshmallowDetail.setOnClickListener { updateSubtotal() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
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
                cbCinnamonDetail.isChecked,
                cbChocolateDetail.isChecked,
                cbMarshmallowDetail.isChecked,
                updateSubtotal()
        )

        presenter.addToCart(itemRm)
    }

    private fun minusQty() {
        if (quantity == 1) return

        quantity -= 1
        updateSubtotal()
        tvQuantityDetail.text = quantity.toString()
    }

    private fun addQty() {
        quantity += 1
        updateSubtotal()
        tvQuantityDetail.text = quantity.toString()
    }

    private fun updateSubtotal(): Int {
        val total = DrinksProvider.calculateTotal(drink, quantity, cbCinnamonDetail.isChecked, cbChocolateDetail.isChecked, cbMarshmallowDetail.isChecked)
        tvSubtotalDetail.text = resources.getString(R.string.price_listing, total)
        return total
    }

}
