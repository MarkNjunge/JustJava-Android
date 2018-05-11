package com.marknkamau.justjava.data.network

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDetails

interface DatabaseService {

    fun saveUserDetails(userDetails: UserDetails, listener: WriteListener)

    fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: DatabaseService.WriteListener)

    fun getUserDefaults(id: String, listener: UserDetailsListener)

    fun placeNewOrder(userId: String?, order: Order, cartItems: List<CartItem>, listener: WriteListener)

    fun getPreviousOrders(userId: String, listener: PreviousOrdersListener)

    fun getOrder(orderId: String, listener: OrderListener)

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
}
