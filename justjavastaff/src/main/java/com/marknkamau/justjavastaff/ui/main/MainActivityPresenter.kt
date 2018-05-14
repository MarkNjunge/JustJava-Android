package com.marknkamau.justjavastaff.ui.main

import com.marknkamau.justjavastaff.data.local.SettingsRespository

import com.marknkamau.justjavastaff.data.network.DataRepository
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.models.User
import timber.log.Timber

internal class MainActivityPresenter(private val view: MainView, private val settings: SettingsRespository, private val dataRepository: DataRepository) {

    fun getOrders() {
        dataRepository.getOrders(object : DataRepository.OrdersListener {
            override fun onSuccess(orders: List<Order>) {
                if (orders.isEmpty()) {
                    view.displayNoOrders()
                } else {
                    val statusSettings = settings.getStatusSettings()
                    val filteredOrders = mutableListOf<Order>()
                    orders.forEach {
                        if (it.status == OrderStatus.PENDING && statusSettings.pending)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.INPROGRESS && statusSettings.inProgress)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.COMPLETED && statusSettings.completed)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.DELIVERED && statusSettings.delivered)
                            filteredOrders.add(it)
                        if (it.status == OrderStatus.CANCELLED && statusSettings.cancelled)
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
