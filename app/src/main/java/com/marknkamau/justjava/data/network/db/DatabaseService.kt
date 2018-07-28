package com.marknkamau.justjava.data.network.db

import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.data.models.UserDetails

interface DatabaseService {

    fun saveUserDetails(userDetails: UserDetails, listener: WriteListener)

    fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: WriteListener)

    fun getUserDefaults(id: String, listener: UserDetailsListener)

    fun placeNewOrder(order: Order, orderItems: List<OrderItem>, listener: WriteListener)

    fun getPreviousOrders(userId: String, listener: PreviousOrdersListener)

    fun getOrderItems(orderId: String, listener: DatabaseService.OrderItemsListener)

    fun savePaymentRequest(merchantRequestId: String, checkoutRequestId: String, orderId: String, customerId:String)

    interface DatabaseListener {
        fun onError(reason: String)
    }

    interface UserDetailsListener : DatabaseListener {
        fun onSuccess(userDetails: UserDetails)
    }

    interface WriteListener : DatabaseListener {
        fun onSuccess()
    }

    interface PreviousOrdersListener : DatabaseListener {
        fun onSuccess(previousOrders: MutableList<Order>)
    }

    interface OrderListener : DatabaseListener {
        fun taskSuccessful(deliveryAddress: String, timestamp: String, totalPrice: String, orderStatus: String)
    }

    interface OrderItemsListener : DatabaseListener {
        fun onSuccess(items: List<OrderItem>)
    }
}
