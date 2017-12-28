package com.marknkamau.justjavastaff.ui.main

import com.marknkamau.justjavastaff.models.Order

interface MainView {
    fun displayMessage(message: String)
    fun displayAvailableOrders(orders: MutableList<Order>)
    fun displayNoOrders()
}
