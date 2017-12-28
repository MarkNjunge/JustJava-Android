package com.marknkamau.justjavastaff.data.network

import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface OrdersRepository {
    fun getOrders(listener: OrdersListener)

    fun getOrderItems(orderId: String, listener: OrderItemsListener)

    fun updateOderStatus(orderId: String, status: OrderStatus, listener: BasicListener)

    interface BaseListener {
        fun onError(reason: String)
    }

    interface BasicListener : BaseListener {
        fun onSuccess()
    }

    interface OrdersListener : BaseListener {
        fun onSuccess(orders: List<Order>)
    }

    interface OrderItemsListener : BaseListener {
        fun onSuccess(items: List<OrderItem>)
    }
}
