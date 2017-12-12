package com.marknkamau.justjava

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.data.network.DatabaseService

object MockDatabase : DatabaseService{
    override fun getUserDefaults(listener: DatabaseService.UserDetailsListener) {
        listener.taskSuccessful("","", "")
        listener.taskFailed("")
    }

    override fun placeNewOrder(order: Order, cartItems: List<CartItem>, listener: DatabaseService.UploadListener) {
        listener.taskSuccessful()
        listener.taskFailed("")
    }

    override fun getPreviousOrders(listener: DatabaseService.PreviousOrdersListener) {
        
    }

    override fun getOrder(orderId: String, listener: DatabaseService.OrderListener) {
        
    }

    override fun setUserDefaults(userDefaults: UserDefaults, listener: DatabaseService.UploadListener) {
        listener.taskSuccessful()
        listener.taskFailed("")
    }

}
