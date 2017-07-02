package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.RealmUtils

internal class DrinkDetailsActivityPresenter(private val activityView: DrinkDetailsActivityView, private val preferences: PreferencesRepository) {

    fun addToCart(cartItem: CartItem) {
        RealmUtils().saveNewItem(cartItem)
        activityView.displayMessage("Item added to cart")
        activityView.finishActivity()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        preferences.clearDefaults()
    }
}
