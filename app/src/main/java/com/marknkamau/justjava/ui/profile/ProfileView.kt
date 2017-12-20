package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDetails

internal interface ProfileView {
    fun displayUserDetails(userDetails: UserDetails)
    fun showProgressBar()
    fun hideProgressBar()
    fun displayNoPreviousOrders()
    fun displayPreviousOrders(orderList: MutableList<PreviousOrder>)
    fun displayMessage(message: String?)
}
