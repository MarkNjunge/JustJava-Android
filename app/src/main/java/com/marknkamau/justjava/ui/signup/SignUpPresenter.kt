package com.marknkamau.justjava.ui.signup

import android.os.Handler
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.network.AuthenticationService
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import com.marknkamau.justjava.network.DatabaseService
import com.marknkamau.justjava.network.DatabaseServiceImpl

internal class SignUpPresenter(private val activityView: SignUpView, private val preferences: PreferencesRepository) {

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        AuthenticationServiceImpl.createUser(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                signInUser(email, password, name, phone, address)
            }

            override fun actionFailed(response: String?) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun signInUser(email: String, password: String, name: String, phone: String, address: String) {
        AuthenticationServiceImpl.signIn(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                setUserDisplayName(name, phone, address)
            }

            override fun actionFailed(response: String?) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun setUserDisplayName(name: String, phone: String, address: String) {
        AuthenticationServiceImpl.setUserDisplayName(name, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                DatabaseServiceImpl.setUserDefaults(UserDefaults(name, phone, address), object : DatabaseService.UploadListener {
                    override fun taskSuccessful() {
                        activityView.enableUserInteraction()
                        preferences.saveDefaults(UserDefaults(name, phone, address))
                        activityView.displayMessage("Sign up successfully")
                        Handler().postDelayed({ activityView.finishActivity() }, 500)

                    }

                    override fun taskFailed(reason: String?) {
                        activityView.enableUserInteraction()
                        activityView.displayMessage(reason)
                    }
                })

            }

            override fun actionFailed(response: String?) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }
}
