package com.marknkamau.justjava.ui.checkout

import com.google.firebase.auth.FirebaseUser
import com.marknkamau.justjava.models.UserDefaults

interface CheckoutView {
    fun invalidateMenu()
    fun setDisplayToLoggedIn(user: FirebaseUser, userDefaults: UserDefaults)
    fun setDisplayToLoggedOut()
    fun showUploadBar()
    fun hideUploadBar()
    fun finishActivity()
    fun showMessage(message: String?)
}
