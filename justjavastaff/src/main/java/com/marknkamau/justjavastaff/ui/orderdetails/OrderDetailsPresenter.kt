package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknkamau.justjavastaff.data.network.DataRepository
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.models.User

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class OrderDetailsPresenter(private val view: OrderDetailsView, private val dataRepository: DataRepository) {

    fun getOrderItems(orderId: String) {
        dataRepository.getOrderItems(orderId, object : DataRepository.OrderItemsListener {
            override fun onSuccess(items: List<OrderItem>) {
                view.displayOrderItems(items.toMutableList())
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }

    fun getUserDetails(userId: String) {
        dataRepository.getCustomerDetails(userId, object : DataRepository.UserListener {
            override fun onError(reason: String) {
                view.displayMessage(reason)
            }

            override fun onSuccess(user: User) {
                view.setUserDetails(user)
            }

        })
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        dataRepository.updateOderStatus(orderId, status, object : DataRepository.BasicListener {
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