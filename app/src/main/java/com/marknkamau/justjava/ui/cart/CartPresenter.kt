package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

internal class CartPresenter(private val view: CartView,
                             private val auth: AuthService,
                             private val preferences: PreferencesRepository,
                             private val cart: CartDao,
                             private val orderService: OrderService,
                             mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    fun getSignInStatus() {
        if (auth.isSignedIn()) {
            view.setDisplayToLoggedIn(preferences.getUserDetails())
        } else {
            view.setDisplayToLoggedOut()
        }
    }

    fun loadItems() {
        uiScope.launch {
            try {
                val items = cart.getAll()
                if (items.size > 0) {
                    view.displayCart(items)
                    var total = 0
                    items.forEach { item -> total += item.itemPrice }
                    view.displayCartTotal(total)
                } else {
                    view.displayEmptyCart()
                }
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message)
            }
        }
    }

    fun clearCart() {
        uiScope.launch {
            cart.deleteAll()
            view.displayEmptyCart()
        }
    }

    @SuppressLint("CheckResult")
    fun deleteItem(item: CartItem) {
        uiScope.launch {
            cart.deleteItem(item)
            view.displayMessage("Item deleted")
            loadItems()
        }
    }

    @SuppressLint("CheckResult")
    fun updateItem(item: CartItem) {
        uiScope.launch {
            cart.updateItem(item)
            view.displayMessage("Cart updated")
            loadItems()
        }
    }

    fun placeOrder(address: String, comments: String, payMpesa: Boolean) {
        view.showLoadingBar()

        uiScope.launch {

            try {
                val (order, orderItems) = createOrderObject(address, comments, payMpesa)
                orderService.placeNewOrder(order, orderItems)

                cart.deleteAll()
                view.hideLoadingBar()
                view.displayMessage("Order placed")
                view.finishActivity(order)
            } catch (e: Exception) {
                view.hideLoadingBar()
                view.displayMessage(e.message)
            }
        }
    }

    private suspend fun createOrderObject(address: String, comments: String, payMpesa: Boolean): Pair<Order,List<OrderItem>> {
        val orderId = UUID.randomUUID().toString().replace("-", "").subSequence(0, 10).toString().toUpperCase()
        val paymentMethod = if (payMpesa) "mpesa" else "cash"

        val orderItems = cart.getAll().map { it.toOrderItem() }
        val total = orderItems.fold(0, { acc, orderItem -> acc + orderItem.itemPrice })
        return Pair(Order(orderId, auth.getCurrentUser().userId, orderItems.size, total, address, comments, paymentMethod = paymentMethod),orderItems)
    }
}


