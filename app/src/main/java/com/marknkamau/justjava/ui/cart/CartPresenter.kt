package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.RealmUtils

internal class CartPresenter(private val activityView: CartView, private val preferences: PreferencesRepository) {
    private val realmUtils: RealmUtils = RealmUtils()

    fun loadItems() {
        val cartItems = realmUtils.getAllCartItems()
        val totalCost = realmUtils.totalPrice
        activityView.displayCartTotal(totalCost)

        if (cartItems.isEmpty()) {
            activityView.displayEmptyCart()
        } else {
            activityView.displayCart(cartItems)
        }
    }

    fun clearCart() {
        realmUtils.deleteAllItems()
        loadItems()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        preferences.clearDefaults()
    }
}


