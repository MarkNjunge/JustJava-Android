package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknjunge.core.data.firebase.StaffDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.OrderStatus
import com.marknjunge.core.model.UserDetails

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class OrderDetailsPresenter(private val view: OrderDetailsView, private val databaseService: StaffDatabaseService) {

    fun getOrderItems(orderId: String) {
        databaseService.getOrderItems(orderId, object : StaffDatabaseService.OrderItemsListener {
            override fun onSuccess(items: List<OrderItem>) {
                view.displayOrderItems(items.toMutableList())
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }

    fun getUserDetails(userId: String) {
        databaseService.getCustomerDetails(userId, object : StaffDatabaseService.UserListener {
            override fun onError(reason: String) {
                view.displayMessage(reason)
            }

            override fun onSuccess(user: UserDetails) {
                view.setUserDetails(user)
            }

        })
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        databaseService.updateOrderStatus(orderId, status, object : WriteListener {
            override fun onSuccess() {
                view.displayMessage("Updated!")
                view.setOrderStatus(status)
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }
}