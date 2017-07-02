package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil
import com.marknkamau.justjava.utils.RealmUtils

internal class CheckoutActivityPresenter(private val activityView: CheckoutActivityView, private val preferences: PreferencesRepository) {

    init {
        updateLoggedInStatus()
    }

    fun logOut() {
        FirebaseAuthUtils.logOut()
        activityView.setDisplayToLoggedOut()
        activityView.setLoggedInStatus(false)
        activityView.invalidateMenu()
    }

    fun updateLoggedInStatus() {
        if (FirebaseAuthUtils.currentUser != null) {
            val defaults = preferences.getDefaults()
            activityView.setDisplayToLoggedIn(FirebaseAuthUtils.currentUser!!, defaults)
            activityView.setLoggedInStatus(true)
        } else {
            activityView.setDisplayToLoggedOut()
            activityView.setLoggedInStatus(false)
        }
        activityView.invalidateMenu()
    }

    fun placeOrder(order: Order) {
        activityView.showUploadBar()
        val realmUtils = RealmUtils()

        val cartItems = realmUtils.getAllCartItems()
        val itemsCount = cartItems.size
        val totalPrice = realmUtils.totalPrice

        order.itemsCount = itemsCount
        order.totalPrice = totalPrice

        FirebaseDBUtil.placeNewOrder(order, realmUtils.getAllCartItems(), object : FirebaseDBUtil.UploadListener {
            override fun taskSuccessful() {
                activityView.hideUploadBar()
                activityView.showMessage("Order placed")
                activityView.finishActivity()
                realmUtils.deleteAllItems()
            }

            override fun taskFailed(reason: String?) {
                activityView.hideUploadBar()
                activityView.showMessage(reason)
            }
        })

    }
}
