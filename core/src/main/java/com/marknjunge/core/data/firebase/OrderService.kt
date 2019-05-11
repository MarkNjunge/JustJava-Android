package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.OrderStatus

interface OrderService {
    suspend fun placeNewOrder(order: Order, orderItems: List<OrderItem>)

    suspend fun getPreviousOrders(userId: String): List<Order>

    suspend fun getOrderItems(orderId: String): List<OrderItem>

    suspend fun getOrder(orderId: String): Order

    suspend fun getAllOrders(): List<Order>

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)

}