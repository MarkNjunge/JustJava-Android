package com.marknkamau.justjavastaff.ui.main

import com.marknjunge.core.model.Order

interface MainView {
    fun displayMessage(message: String)
    fun displayAvailableOrders(orders: MutableList<Order>)
    fun displayNoOrders()
}
