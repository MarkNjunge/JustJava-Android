package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.data.CartRepositoryImpl

internal class DrinkDetailsPresenter(private val activityView: DrinkDetailsView, private val preferences: PreferencesRepository) {

    fun addToCart(cartItem: CartItem) {
        CartRepositoryImpl().saveNewItem(cartItem)
        activityView.displayMessage("Item added to cart")
        activityView.finishActivity()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        preferences.clearDefaults()
    }
}
