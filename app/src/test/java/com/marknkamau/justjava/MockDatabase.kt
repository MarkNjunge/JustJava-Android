package com.marknkamau.justjava

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDetails
import com.marknkamau.justjava.data.network.DatabaseService

object MockDatabase : DatabaseService{
    override fun getUserDefaults(listener: DatabaseService.UserDetailsListener) {
        listener.onSuccess("","", "")
        listener.onError("")
    }

    override fun placeNewOrder(order: Order, cartItems: List<CartItem>, listener: DatabaseService.WriteListener) {
        listener.onSuccess()
        listener.onError("")
    }

    override fun getPreviousOrders(listener: DatabaseService.PreviousOrdersListener) {
        
    }

    override fun getOrder(orderId: String, listener: DatabaseService.OrderListener) {
        
    }

    override fun saveUserDetails(userDetails: UserDetails, listener: DatabaseService.WriteListener) {
        listener.onSuccess()
        listener.onError("")
    }

}
