package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.OrderStatus
import com.marknjunge.core.model.UserDetails

interface StaffDatabaseService{
    fun getOrders(listener: OrdersListener)

    fun getOrderItems(orderId: String, listener: OrderItemsListener)

    fun updateOrderStatus(orderId: String, status: OrderStatus, listener: WriteListener)

    fun getCustomerDetails(userId: String, listener: UserListener)

    interface OrdersListener : DatabaseListener {
        fun onSuccess(orders: List<Order>)
    }

    interface OrderItemsListener : DatabaseListener {
        fun onSuccess(items: List<OrderItem>)
    }

    interface UserListener : DatabaseListener {
        fun onSuccess(user: UserDetails)
    }
}