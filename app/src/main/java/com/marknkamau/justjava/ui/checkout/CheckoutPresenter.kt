package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.network.DatabaseServiceImpl
import com.marknkamau.justjava.data.CartRepositoryImpl
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.network.DatabaseService

internal class CheckoutPresenter(private val activityView: CheckoutView, auth: AuthenticationService, preferences: PreferencesRepository) {

    init {
        if(auth.isSignedIn()){
            activityView.setDisplayToLoggedIn(auth.getCurrentUser()!!, preferences.getDefaults())
        }else{
            activityView.setDisplayToLoggedOut()
        }
    }

    fun placeOrder(order: Order) {
        activityView.showUploadBar()

        val cartItems = CartRepositoryImpl.getAllCartItems()
        val itemsCount = cartItems.size
        val totalPrice = CartRepositoryImpl.getTotalPrice()

        order.itemsCount = itemsCount
        order.totalPrice = totalPrice

        DatabaseServiceImpl.placeNewOrder(order, CartRepositoryImpl.getAllCartItems(), object : DatabaseService.UploadListener {
            override fun taskSuccessful() {
                activityView.hideUploadBar()
                activityView.showMessage("Order placed")
                activityView.finishActivity()
                CartRepositoryImpl.deleteAllItems()
            }

            override fun taskFailed(reason: String?) {
                activityView.hideUploadBar()
                activityView.showMessage(reason)
            }
        })

    }
}
