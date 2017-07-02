package com.marknkamau.justjava.ui.login

import android.os.Handler
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.models.UserDefaults

import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil

internal class LogInPresenter(private val activityView: LogInView, private val preferences: PreferencesRepository) {

    fun checkSignInStatus() {
        if (FirebaseAuthUtils.currentUser != null) {
            activityView.closeActivity()
        }
    }

    fun signIn(email: String, password: String) {
        activityView.showDialog()
        FirebaseAuthUtils.signIn(email, password, object : FirebaseAuthUtils.AuthActionListener {
            override fun actionSuccessful(response: String) {
                getUserDefaults()
            }

            override fun actionFailed(response: String?) {
                activityView.displayMessage(response)
            }
        })
    }

    private fun getUserDefaults() {
        FirebaseDBUtil.getUserDefaults(object : FirebaseDBUtil.UserDetailsListener {
            override fun taskSuccessful(name: String, phone: String, deliveryAddress: String) {
                preferences.saveDefaults(UserDefaults(name, phone, deliveryAddress))
                activityView.dismissDialog()
                activityView.displayMessage("Sign in successful")
                Handler().postDelayed({ activityView.finishSignUp() }, 500)
            }

            override fun taskFailed(reason: String?) {
                activityView.displayMessage(reason)
            }
        })
    }

    fun resetUserPassword(email: String) {
        FirebaseAuthUtils.sendPasswordResetEmail(email, object : FirebaseAuthUtils.AuthActionListener {
            override fun actionSuccessful(response: String) {
                activityView.displayMessage(response)
            }

            override fun actionFailed(response: String?) {
                activityView.displayMessage(response)
            }
        })
    }
}
