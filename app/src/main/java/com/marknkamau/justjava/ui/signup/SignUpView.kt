package com.marknkamau.justjava.ui.signup

interface SignUpView {
    fun enableUserInteraction()
    fun disableUserInteraction()
    fun displayMessage(message: String)
    fun finishActivity()
}
