package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.BaseView

interface CheckoutView : BaseView {
    fun invalidateMenu()
    fun setDisplayToLoggedIn(userDefaults: UserDefaults)
    fun setDisplayToLoggedOut()
    fun showUploadBar()
    fun hideUploadBar()
    fun finishActivity()
}
