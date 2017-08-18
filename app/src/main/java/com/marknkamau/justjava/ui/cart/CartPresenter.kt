package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartRepository
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.authentication.AuthenticationServiceImpl

internal class CartPresenter(private val activityView: CartView, private val cartRepository: CartRepository) {

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

}


