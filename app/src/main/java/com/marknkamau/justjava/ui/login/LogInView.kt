package com.marknkamau.justjava.ui.login

interface LogInView {
    fun closeActivity()
    fun signIn()
    fun resetUserPassword()
    fun displayMessage(message: String?)
    fun showLoading()
    fun dismissLoading()
    fun finishSignIn()
}
