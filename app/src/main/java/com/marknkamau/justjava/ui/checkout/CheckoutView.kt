package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.models.UserDetails
import com.marknkamau.justjava.ui.BaseView

interface CheckoutView : BaseView {
    fun invalidateMenu()
    fun setDisplayToLoggedIn(userDetails: UserDetails)
    fun setDisplayToLoggedOut()
    fun showUploadBar()
    fun hideUploadBar()
    fun finishActivity()
}
