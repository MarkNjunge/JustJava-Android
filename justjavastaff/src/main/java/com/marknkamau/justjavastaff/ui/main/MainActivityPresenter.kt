package com.marknkamau.justjavastaff.ui.main

import android.content.SharedPreferences

import com.marknkamau.justjavastaff.data.network.OrdersRepository
import com.marknkamau.justjavastaff.models.Order

internal class MainActivityPresenter(private val view: MainView, private val preferences: SharedPreferences, private val ordersRepository: OrdersRepository) {

    fun getOrders() {
        ordersRepository.getOrders(object : OrdersRepository.OrdersListener {
            override fun onSuccess(orders: List<Order>) {
                if (orders.isEmpty()) {
                    view.displayNoOrders()
                } else {
                    view.displayAvailableOrders(orders.toMutableList())
                }
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }
}
