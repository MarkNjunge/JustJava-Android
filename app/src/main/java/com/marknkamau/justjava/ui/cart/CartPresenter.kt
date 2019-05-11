package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

internal class CartPresenter(private val view: CartView,
                             private val cart: CartDao,
                             mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

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
}


