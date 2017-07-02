package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.models.PreviousOrder

internal interface ProfileActivityView {
    fun displayUserDefaults(defaults: Map<String, String>)
    fun showProgressBar()
    fun hideProgressBar()
    fun displayNoPreviousOrders()
    fun displayPreviousOrders(orderList: List<PreviousOrder>)
    fun displayMessage(message: String?)
}
