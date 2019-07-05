package com.marknkamau.justjava.ui.previousOrders

import com.marknjunge.core.model.Order

interface PreviousOrdersView {
    fun displayMessage(message: String)
    fun displayNoOrders()
    fun displayOrders(orders: List<Order>)
}