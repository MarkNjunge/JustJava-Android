package com.marknkamau.justjava.ui.checkout

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.Order
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CheckoutPresenter(private val activityView: CheckoutView,
                                 private val auth: AuthService,
                                 private val preferences: PreferencesRepository,
                                 private val database: ClientDatabaseService,
                                 private val cart: CartDao,
                                 mainDispatcher: CoroutineDispatcher) : BasePresenter(mainDispatcher) {
    fun getSignInStatus() {
        if (auth.isSignedIn()) {
            activityView.setDisplayToLoggedIn(preferences.getUserDetails())
        } else {
            activityView.setDisplayToLoggedOut()
        }
    }

    fun placeOrder(orderId: String, address: String, comments: String, payCash: Boolean) {
        val paymentMethod = if (payCash) "cash" else "mpesa"
        val order = Order(orderId, auth.getCurrentUser().userId, 0, 0, address, comments, paymentMethod = paymentMethod)
        activityView.showUploadBar()

        uiScope.launch {
            val items = cart.getAll()
            placeOrderInternal(items, order)
        }
    }

    private fun placeOrderInternal(items: MutableList<CartItem>, order: Order) {
        val itemsCount = items.size
        var total = 0
        items.forEach { item -> total += item.itemPrice }

        order.itemsCount = itemsCount
        order.totalPrice = total

        val orderItems = mutableListOf<OrderItem>()
        items.forEach {
            orderItems.add(it.toOrderItem())
        }

        database.placeNewOrder(order, orderItems, object : WriteListener {
            override fun onSuccess() {
                uiScope.launch {
                    cart.deleteAll()
                    activityView.hideUploadBar()
                    activityView.displayMessage("Order placed")
                    activityView.finishActivity(order)
                }
            }

            override fun onError(reason: String) {
                activityView.hideUploadBar()
                activityView.displayMessage(reason)
            }
        })

    }
}
