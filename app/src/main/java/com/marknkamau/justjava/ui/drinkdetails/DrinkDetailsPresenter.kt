package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.CartRepository
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import com.marknkamau.justjava.data.CartRepositoryImpl
import com.marknkamau.justjava.network.AuthenticationService

internal class DrinkDetailsPresenter(val activityView: DrinkDetailsView,
                                     val preferences: PreferencesRepository,
                                     val auth: AuthenticationService,
                                     val cartRepository: CartRepository) {

    fun getSignInStatus() {
        activityView.setSignInStatus(auth.isSignedIn())
    }

    fun addToCart(cartItem: CartItem) {
        cartRepository.saveNewItem(cartItem)
        activityView.displayMessage("Item added to cart")
        activityView.finishActivity()
    }

    fun logUserOut() {
        AuthenticationServiceImpl.logOut()
        preferences.clearDefaults()
        activityView.setSignInStatus(auth.isSignedIn())
    }
}
