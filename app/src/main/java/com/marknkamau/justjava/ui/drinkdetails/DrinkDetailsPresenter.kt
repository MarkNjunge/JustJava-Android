package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.CartRepository
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.authentication.AuthenticationService

internal class DrinkDetailsPresenter(val activityView: DrinkDetailsView,
                                     val cartRepository: CartRepository) {

    fun addToCart(cartItem: CartItem) {
        cartRepository.saveNewItem(cartItem)
        activityView.displayMessage("Item added to cart")
        activityView.finishActivity()
    }
}
