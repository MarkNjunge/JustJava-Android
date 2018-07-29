package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.data.models.UserDetails
import com.marknkamau.justjava.ui.BaseView

interface CheckoutView : BaseView {
    fun invalidateMenu()
    fun setDisplayToLoggedIn(userDetails: UserDetails)
    fun setDisplayToLoggedOut()
    fun showUploadBar()
    fun hideUploadBar()
    fun finishActivity(order: Order)
}
