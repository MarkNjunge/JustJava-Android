package com.marknkamau.justjava.ui.profile

import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil

internal class ProfileActivityPresenter(private val activityView: ProfileActivityView, private val preferencesRepository: PreferencesRepository) {

    init {
        getUserDefaults()
        getPreviousOrders()
    }

    private fun getUserDefaults() {
        activityView.displayUserDefaults(preferencesRepository.getDefaults())
    }

    private fun getPreviousOrders() {
        FirebaseDBUtil.getPreviousOrders(object : FirebaseDBUtil.PreviousOrdersListener {
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
        FirebaseAuthUtils.setUserDisplayName(name, object : FirebaseAuthUtils.AuthActionListener {
            override fun actionSuccessful(response: String) {
                FirebaseDBUtil.setUserDefaults(UserDefaults(name, phone, address), object : FirebaseDBUtil.UploadListener {
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
        FirebaseAuthUtils.logOut()
        preferencesRepository.clearDefaults()
    }
}
