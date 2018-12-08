package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.*

interface StaffDatabaseService {
    fun getOrders(listener: OrdersListener)

    fun getOrderItems(orderId: String, listener: OrderItemsListener)

    fun updateOrderStatus(orderId: String, status: OrderStatus, listener: WriteListener)

    fun getCustomerDetails(userId: String, listener: UserListener)

    fun getPayments(listener: PaymentsListener)

    interface OrdersListener : DatabaseListener {
        fun onSuccess(orders: List<Order>)
    }

    interface OrderItemsListener : DatabaseListener {
        fun onSuccess(items: List<OrderItem>)
    }

    interface UserListener : DatabaseListener {
        fun onSuccess(user: UserDetails)
    }

    interface PaymentsListener : DatabaseListener {
        fun onSuccess(payments: List<Payment>)
    }
}