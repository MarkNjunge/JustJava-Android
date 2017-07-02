package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults

internal interface ProfileActivityView {
    fun displayUserDefaults(userDefaults: UserDefaults)
    fun showProgressBar()
    fun hideProgressBar()
    fun displayNoPreviousOrders()
    fun displayPreviousOrders(orderList: List<PreviousOrder>)
    fun displayMessage(message: String?)
}
