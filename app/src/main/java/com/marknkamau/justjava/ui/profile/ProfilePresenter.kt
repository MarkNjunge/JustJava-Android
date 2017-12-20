package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.DatabaseService
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDetails

internal class ProfilePresenter(private val activityView: ProfileView,
                                private val preferencesRepository: PreferencesRepository,
                                private val authenticationService: AuthenticationService,
                                private val databaseService: DatabaseService) {

    init {
        getUserDetails()
        getPreviousOrders()
    }

    lateinit var userDetails: UserDetails

    private fun getUserDetails() {
        userDetails = preferencesRepository.getUserDetails()
        activityView.displayUserDetails(userDetails)
    }

    private fun getPreviousOrders() {
        databaseService.getPreviousOrders(object : DatabaseService.PreviousOrdersListener {
            override fun taskSuccessful(previousOrders: MutableList<PreviousOrder>) {
                activityView.displayPreviousOrders(previousOrders)
            }

            override fun noValuesPresent() {
                activityView.displayNoPreviousOrders()
            }

            override fun onError(reason: String) {
                activityView.displayMessage(reason)
            }
        })
    }

    fun updateUserDetails(name: String, phone: String, address: String) {
        activityView.showProgressBar()

        authenticationService.setUserDisplayName(name, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                databaseService.updateUserDetails(userDetails.id, name, phone, address, object : DatabaseService.WriteListener {
                    override fun onSuccess() {
                        val newUserDetails = UserDetails(userDetails.id, userDetails.email,name, phone, address)

                        preferencesRepository.saveUserDetails(newUserDetails)
                        activityView.hideProgressBar()
                        activityView.displayMessage("Default values updated")
                    }

                    override fun onError(reason: String) {

                    }
                })
            }

            override fun actionFailed(response: String?) {
                activityView.displayMessage(response)
            }
        })
    }

    fun logUserOut() {
        AuthenticationServiceImpl.logOut()
        preferencesRepository.clearUserDetails()
    }
}
