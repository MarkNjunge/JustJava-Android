package com.marknkamau.justjava.ui.signup

import com.marknkamau.justjava.data.network.authentication.AuthenticationService
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknkamau.justjava.data.models.UserDetails

internal class SignUpPresenter(private val activityView: SignUpView,
                               private val preferences: PreferencesRepository,
                               private val auth: AuthenticationService,
                               private val database: DatabaseService) {

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        auth.createUser(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                signInUser(email, password, name, phone, address)
            }

            override fun actionFailed(response: String) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun signInUser(email: String, password: String, name: String, phone: String, address: String) {
        auth.signIn(email, password, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                setUserDisplayName(response, email, name, phone, address)
            }

            override fun actionFailed(response: String) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }

    private fun setUserDisplayName(id:String, email: String, name: String, phone: String, address: String) {
        auth.setUserDisplayName(name, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                val userDetails = UserDetails(id, email, name, phone, address)

                database.saveUserDetails(userDetails, object : DatabaseService.WriteListener {
                    override fun onSuccess() {
                        activityView.enableUserInteraction()
                        preferences.saveUserDetails(userDetails)
                        activityView.displayMessage("Sign up successfully")
                        activityView.finishActivity()
                    }

                    override fun onError(reason: String) {
                        activityView.enableUserInteraction()
                        activityView.displayMessage(reason)
                    }
                })

            }

            override fun actionFailed(response: String) {
                activityView.enableUserInteraction()
                activityView.displayMessage(response)
            }
        })
    }
}
