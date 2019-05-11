package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.model.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class OrderDetailsPresenter(private val view: OrderDetailsView, private val orderService: OrderService, private val userService: UserService) {

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    fun getOrderItems(orderId: String) {
        uiScope.launch {
            try {
                val orderItems = orderService.getOrderItems(orderId)
                view.displayOrderItems(orderItems.toMutableList())
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error getting order items")
            }
        }
    }

    fun getUserDetails(userId: String) {
        uiScope.launch {
            try {
                val userDetails = userService.getUserDetails(userId)
                view.setUserDetails(userDetails)
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error getting user details")

            }
        }
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        uiScope.launch {
            try {
                orderService.updateOrderStatus(orderId, status)
                view.displayMessage("Updated!")
                view.setOrderStatus(status)
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error updating status")
            }
        }
    }
}