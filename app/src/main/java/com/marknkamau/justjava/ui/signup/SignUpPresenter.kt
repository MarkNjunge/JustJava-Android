package com.marknkamau.justjava.ui.signup

import com.marknkamau.justjava.data.local.PreferencesRepository

import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.network.DatabaseService

internal class SignUpPresenter(private val activityView: SignUpView,
                               private val preferences: PreferencesRepository,
                               private val auth: AuthenticationService,
                               private val database: DatabaseService) {

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        auth.createUser(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String?) {
                signInUser(email, password, name, phone, address)
            }

            override fun actionFailed(response: String?) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun signInUser(email: String, password: String, name: String, phone: String, address: String) {
        auth.signIn(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String?) {
                setUserDisplayName(name, phone, address)
            }

            override fun actionFailed(response: String?) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun setUserDisplayName(name: String, phone: String, address: String) {
        auth.setUserDisplayName(name, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String?) {
                database.setUserDefaults(UserDefaults(name, phone, address), object : DatabaseService.UploadListener {
                    override fun taskSuccessful() {
                        activityView.enableUserInteraction()
                        preferences.saveDefaults(UserDefaults(name, phone, address))
                        activityView.displayMessage("Sign up successfully")
                        activityView.finishActivity()
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
