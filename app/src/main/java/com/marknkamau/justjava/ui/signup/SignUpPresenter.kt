package com.marknkamau.justjava.ui.signup

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknjunge.core.model.UserDetails

internal class SignUpPresenter(private val activityView: SignUpView,
                               private val preferences: PreferencesRepository,
                               private val auth: AuthService,
                               private val database: ClientDatabaseService) {

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        auth.createUser(email, password, object : AuthService.AuthActionListener {
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
        auth.signIn(email, password, object : AuthService.AuthActionListener {
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
        auth.setUserDisplayName(name, object : AuthService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                val userDetails = UserDetails(id, email, name, phone, address)

                database.saveUserDetails(userDetails, object : ClientDatabaseService.WriteListener {
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
