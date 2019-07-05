package com.marknkamau.justjava.ui.previousOrders

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PreviousOrdersPresenter(private val view: PreviousOrdersView,
                              private val authenticationService: AuthService,
                              private val orderService: OrderService,
                              mainDispatcher: CoroutineDispatcher
): BasePresenter(mainDispatcher){

    fun getPreviousOrders() {
        uiScope.launch {
            try {
                val previousOrders = orderService.getPreviousOrders(authenticationService.getCurrentUser().userId)
                if (previousOrders.isEmpty()) {
                    view.displayNoOrders()
                } else {
                    val sorted = previousOrders.sortedBy { it.date }.reversed().toMutableList()
                    view.displayOrders(sorted)
                }
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error getting previous orders")
            }
        }
    }

}