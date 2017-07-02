package com.marknkamau.justjava.ui.cart

import android.content.SharedPreferences

import com.marknkamau.justjava.utils.Constants
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.RealmUtils

internal class CartActivityPresenter(private val activityView: CartActivityView, private val sharedPreferences: SharedPreferences) {
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
        val editor = sharedPreferences.edit()
        editor.remove(Constants.DEF_NAME)
        editor.remove(Constants.DEF_PHONE)
        editor.remove(Constants.DEF_ADDRESS)
        editor.apply()
    }
}


