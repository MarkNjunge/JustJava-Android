package com.marknkamau.justjavastaff.ui.orders

import com.marknjunge.core.model.Order

interface OrdersView {
    fun displayMessage(message: String)
    fun displayAvailableOrders(orders: MutableList<Order>)
    fun displayNoOrders()
}
