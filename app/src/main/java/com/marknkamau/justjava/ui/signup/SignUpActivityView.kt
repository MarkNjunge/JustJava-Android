package com.marknkamau.justjava.ui.signup

interface SignUpActivityView {
    fun enableUserInteraction()
    fun disableUserInteraction()
    fun displayMessage(message: String?)
    fun finishActivity()
}
