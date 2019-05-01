package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

internal class CartPresenter(private val activityView: CartView, private val cart: CartDao, mainDispatcher: CoroutineDispatcher)
    : BasePresenter(mainDispatcher) {

    fun loadItems() {
        uiScope.launch {
            try {
                val items = cart.getAll()
                if (items.size > 0) {
                    activityView.displayCart(items)
                    var total = 0
                    items.forEach { item -> total += item.itemPrice }
                    activityView.displayCartTotal(total)
                } else {
                    activityView.displayEmptyCart()
                }
            } catch (e: Exception) {
                Timber.e(e)
                activityView.displayMessage(e.message)
            }
        }
    }

    fun clearCart() {
        uiScope.launch {
            cart.deleteAll()
            activityView.displayEmptyCart()
        }
    }

    @SuppressLint("CheckResult")
    fun deleteItem(item: CartItem) {
        uiScope.launch {
            cart.deleteItem(item)
            activityView.displayMessage("Item deleted")
            loadItems()
        }
    }

    @SuppressLint("CheckResult")
    fun updateItem(item: CartItem) {
        uiScope.launch {
            cart.updateItem(item)
            activityView.displayMessage("Cart updated")
            loadItems()
        }
    }
}


