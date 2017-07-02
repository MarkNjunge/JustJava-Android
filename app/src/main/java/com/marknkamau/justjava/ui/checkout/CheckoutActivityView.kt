package com.marknkamau.justjava.ui.checkout

import com.google.firebase.auth.FirebaseUser

interface CheckoutActivityView {
    fun setLoggedInStatus(status: Boolean?)
    fun invalidateMenu()
    fun setDisplayToLoggedIn(user: FirebaseUser, defaults: Map<String, String>)
    fun setDisplayToLoggedOut()
    fun showUploadBar()
    fun hideUploadBar()
    fun finishActivity()
    fun showMessage(message: String?)
}
