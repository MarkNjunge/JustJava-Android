package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.UserDetails

interface ClientDatabaseService {
    fun saveUserDetails(userDetails: UserDetails, listener: WriteListener)

    fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: WriteListener)

    fun updateUserFcmToken(userId: String, listener: WriteListener)

    fun getUserDefaults(id: String, listener: UserDetailsListener)

    fun placeNewOrder(order: Order, orderItems: List<OrderItem>, listener: WriteListener)

    fun getPreviousOrders(userId: String, listener: PreviousOrdersListener)

    fun getOrderItems(orderId: String, listener: OrderItemsListener)

    fun savePaymentRequest(merchantRequestId: String, checkoutRequestId: String, orderId: String, customerId: String)

    fun getOrder(orderId: String, listener: OrderListener)

    interface UserDetailsListener : DatabaseListener {
        fun onSuccess(userDetails: UserDetails)
    }

    interface PreviousOrdersListener : DatabaseListener {
        fun onSuccess(previousOrders: MutableList<Order>)
    }

    interface OrderItemsListener : DatabaseListener {
        fun onSuccess(items: List<OrderItem>)
    }

    interface OrderListener : DatabaseListener {
        fun onSuccess(order: Order)
    }

}