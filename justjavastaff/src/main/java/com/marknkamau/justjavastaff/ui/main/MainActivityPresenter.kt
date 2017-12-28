package com.marknkamau.justjavastaff.ui.main

import com.marknkamau.justjavastaff.data.local.SettingsRespository

import com.marknkamau.justjavastaff.data.network.OrdersRepository
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderStatus

internal class MainActivityPresenter(private val view: MainView, private val settings: SettingsRespository, private val ordersRepository: OrdersRepository) {

    fun getOrders() {
        ordersRepository.getOrders(object : OrdersRepository.OrdersListener {
            override fun onSuccess(orders: List<Order>) {
                if (orders.isEmpty()) {
                    view.displayNoOrders()
                } else {
                    val statusSettings = settings.getStatusSettings()
                    val filteredOrders = mutableListOf<Order>()
                    orders.forEach {
                        if (it.status == OrderStatus.PENDING.name && statusSettings.pending)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.INPROGRESS.name && statusSettings.inProgress)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.COMPLETED.name && statusSettings.completed)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.DELIVERED.name && statusSettings.delivered)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.CANCELLED.name && statusSettings.cancelled)
                            filteredOrders.add(it)
                    }
                    view.displayAvailableOrders(filteredOrders)
                }
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }
}
