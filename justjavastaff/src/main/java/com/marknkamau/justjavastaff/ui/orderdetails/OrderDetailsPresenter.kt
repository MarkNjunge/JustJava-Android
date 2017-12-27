package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknkamau.justjavastaff.data.network.OrdersRepository
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class OrderDetailsPresenter(private val view: OrderDetailsView, private val ordersRepository: OrdersRepository) {

    fun getOrderItems(orderId: String) {
        ordersRepository.getOrderItems(orderId, object : OrdersRepository.OrderItemsListener {
            override fun onSuccess(items: MutableList<OrderItem>) {
                view.displayOrderItems(items)
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        ordersRepository.updateOderStatus(orderId, status, object : OrdersRepository.BasicListener {
            override fun onSuccess() {
                view.displayMessage("Updated!")
                view.setOrderStatus(status)
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }
}