package com.marknkamau.justjavastaff.ui.orders

import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.model.OrderStatus
import com.marknkamau.justjavastaff.data.local.SettingsRespository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class OrdersFragmentPresenter(private val view: OrdersView,
                                       private val settings: SettingsRespository,
                                       private val orderService: OrderService
) {

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    fun getOrders() {
        uiScope.launch {
            try {
                val orders = orderService.getAllOrders()
                if (orders.isEmpty()) {
                    view.displayNoOrders()
                } else {
                    val statusSettings = settings.getStatusSettings()
                    val filteredList = orders.filter {
                        it.status == OrderStatus.PENDING && statusSettings.pending ||
                                it.status == OrderStatus.INPROGRESS && statusSettings.inProgress ||
                                it.status == OrderStatus.COMPLETED && statusSettings.completed ||
                                it.status == OrderStatus.DELIVERED && statusSettings.delivered ||
                                it.status == OrderStatus.CANCELLED && statusSettings.cancelled
                    }.sortedBy { it.date }.reversed()

                    view.displayAvailableOrders(filteredList.toMutableList())
                }
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error getting orders")
            }
        }
    }
}
