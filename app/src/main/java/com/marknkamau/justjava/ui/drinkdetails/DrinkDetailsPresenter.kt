package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

internal class DrinkDetailsPresenter(private val activityView: DrinkDetailsView,
                                     private val cart: CartDao,
                                     mainDispatcher: CoroutineDispatcher)
    : BasePresenter(mainDispatcher) {

    fun addToCart(item: CartItem) {
        uiScope.launch {
            try {
                cart.addItem(item)
                activityView.displayMessage("Item added to cart")
                activityView.finishActivity()
            } catch (e: Exception) {
                Timber.e(e)
                activityView.displayMessage(e.message)
            }
        }
    }
}
