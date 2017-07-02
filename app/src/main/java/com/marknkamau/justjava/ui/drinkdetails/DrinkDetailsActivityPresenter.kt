package com.marknkamau.justjava.ui.drinkdetails

import android.content.SharedPreferences

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.utils.Constants
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.RealmUtils

internal class DrinkDetailsActivityPresenter(private val activityView: DrinkDetailsActivityView, private val sharedPreferences: SharedPreferences) {

    fun addToCart(cartItem: CartItem) {
        RealmUtils().saveNewItem(cartItem)
        activityView.displayMessage("Item added to cart")
        activityView.finishActivity()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        val editor = sharedPreferences.edit()
        editor.remove(Constants.DEF_NAME)
        editor.remove(Constants.DEF_PHONE)
        editor.remove(Constants.DEF_ADDRESS)
        editor.apply()
    }
}
