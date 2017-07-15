package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import com.marknkamau.justjava.network.DatabaseServiceImpl
import com.marknkamau.justjava.data.CartRepositoryImpl
import com.marknkamau.justjava.network.DatabaseService

internal class CheckoutPresenter(private val activityView: CheckoutView, private val preferences: PreferencesRepository) {

    init {
        updateLoggedInStatus()
    }

    fun logOut() {
        AuthenticationServiceImpl.logOut()
        activityView.setDisplayToLoggedOut()
        activityView.setLoggedInStatus(false)
        activityView.invalidateMenu()
    }

    fun updateLoggedInStatus() {
        if (AuthenticationServiceImpl.currentUser != null) {
            val defaults = preferences.getDefaults()
            activityView.setDisplayToLoggedIn(AuthenticationServiceImpl.currentUser!!, defaults)
            activityView.setLoggedInStatus(true)
        } else {
            activityView.setDisplayToLoggedOut()
            activityView.setLoggedInStatus(false)
        }
        activityView.invalidateMenu()
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
