package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartRepository
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.data.CartRepositoryImpl

internal class CartPresenter(private val activityView: CartView, private val preferences: PreferencesRepository, private val cartRepository: CartRepository) {

    fun loadItems() {
        val cartItems = cartRepository.getAllCartItems()
        val totalCost = cartRepository.getTotalPrice()
        activityView.displayCartTotal(totalCost)

        if (cartItems.isEmpty()) {
            activityView.displayEmptyCart()
        } else {
            activityView.displayCart(cartItems)
        }
    }

    fun clearCart() {
        cartRepository.deleteAllItems()
        loadItems()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        preferences.clearDefaults()
    }
}


