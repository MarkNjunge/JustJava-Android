package com.marknkamau.justjava.ui.profile

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails

internal interface ProfileView {
    fun displayUserDetails(userDetails: UserDetails)
    fun showProfileProgressBar()
    fun hideProfileProgressBar()
    fun displayNoPreviousOrders()
    fun displayPreviousOrders(orderList: MutableList<Order>)
    fun displayMessage(message: String)
}
