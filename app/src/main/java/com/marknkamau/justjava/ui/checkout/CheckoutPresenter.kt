package com.marknkamau.justjava.ui.checkout

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.Order
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CheckoutPresenter(private val view: CheckoutView,
                                 private val auth: AuthService,
                                 private val preferences: PreferencesRepository,
                                 private val orderService: OrderService,
                                 private val cart: CartDao,
                                 mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    fun getSignInStatus() {
        if (auth.isSignedIn()) {
            view.setDisplayToLoggedIn(preferences.getUserDetails())
        } else {
            view.setDisplayToLoggedOut()
        }
    }

    fun placeOrder(orderId: String, address: String, comments: String, payCash: Boolean) {
        val paymentMethod = if (payCash) "cash" else "mpesa"
        val order = Order(orderId, auth.getCurrentUser().userId, 0, 0, address, comments, paymentMethod = paymentMethod)
        view.showUploadBar()

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

        uiScope.launch {
            try {
                orderService.placeNewOrder(order, orderItems)

                cart.deleteAll()
                view.hideUploadBar()
                view.displayMessage("Order placed")
                view.finishActivity(order)
            } catch (e: Exception) {
                view.hideUploadBar()
                view.displayMessage(e.message)
            }
        }
    }
}
