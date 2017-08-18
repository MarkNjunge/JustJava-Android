package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.network.DatabaseService
import com.marknkamau.justjava.network.DatabaseServiceImpl

internal class ProfilePresenter(private val activityView: ProfileView, private val preferencesRepository: PreferencesRepository) {

    init {
        getUserDefaults()
        getPreviousOrders()
    }

    private fun getUserDefaults() {
        activityView.displayUserDefaults(preferencesRepository.getDefaults())
    }

    private fun getPreviousOrders() {
        DatabaseServiceImpl.getPreviousOrders(object : DatabaseService.PreviousOrdersListener {
            override fun taskSuccessful(previousOrders: MutableList<PreviousOrder>) {
                activityView.displayPreviousOrders(previousOrders)
            }

            override fun noValuesPresent() {
                activityView.displayNoPreviousOrders()
            }

            override fun taskFailed(reason: String?) {
                activityView.displayMessage(reason)
            }
        })
    }

    fun updateUserDefaults(name: String, phone: String, address: String) {
        activityView.showProgressBar()
        AuthenticationServiceImpl.setUserDisplayName(name, object : AuthenticationService.AuthActionListener {
            override fun actionSuccessful(response: String?) {
                DatabaseServiceImpl.setUserDefaults(UserDefaults(name, phone, address), object : DatabaseService.UploadListener {
                    override fun taskSuccessful() {
                        preferencesRepository.saveDefaults(UserDefaults(name, phone, address))
                        activityView.hideProgressBar()
                        activityView.displayMessage("Default values updated")
                    }

                    override fun taskFailed(reason: String?) {
                        activityView.hideProgressBar()
                        activityView.displayMessage(reason)
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
        preferencesRepository.clearDefaults()
    }
}
