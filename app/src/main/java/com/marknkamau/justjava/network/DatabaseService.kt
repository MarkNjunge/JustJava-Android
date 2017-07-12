package com.marknkamau.justjava.network

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults

interface DatabaseService{

    fun getUserDefaults(listener: UserDetailsListener)

    fun placeNewOrder(order: Order, cartItems: List<CartItem>, listener: UploadListener)

    fun getPreviousOrders(listener: PreviousOrdersListener)

    fun getOrder(orderId: String, listener: OrderListener)

    fun setUserDefaults(userDefaults: UserDefaults, listener: UploadListener)

    interface DatabaseListener {
        fun taskFailed(reason: String?)
    }

    interface UserDetailsListener : DatabaseListener {
        fun taskSuccessful(name: String, phone: String, deliveryAddress: String)
    }

    interface UploadListener : DatabaseListener {
        fun taskSuccessful()
    }

    interface PreviousOrdersListener : DatabaseListener {
        fun taskSuccessful(previousOrders: MutableList<PreviousOrder>)

        fun noValuesPresent()
    }

    interface OrderListener : DatabaseListener {
        fun taskSuccessful(deliveryAddress: String, timestamp: String, totalPrice: String, orderStatus: String)
    }
}
