package com.marknkamau.justjava.ui.login

import android.content.SharedPreferences
import android.os.Handler

import com.marknkamau.justjava.utils.Constants
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil

internal class LogInActivityPresenter(private val activityView: LogInActivityView, private val sharedPreferences: SharedPreferences) {

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
                saveToSharedPreferences(name, phone, deliveryAddress)
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

    private fun saveToSharedPreferences(name: String, phone: String, address: String) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.DEF_NAME, name)
        editor.putString(Constants.DEF_PHONE, phone)
        editor.putString(Constants.DEF_ADDRESS, address)

        editor.apply()
    }
}
