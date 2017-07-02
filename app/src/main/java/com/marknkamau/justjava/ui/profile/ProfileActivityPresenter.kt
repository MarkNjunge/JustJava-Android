package com.marknkamau.justjava.ui.profile

import android.content.SharedPreferences

import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.utils.Constants
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil

import java.util.HashMap

internal class ProfileActivityPresenter(private val activityView: ProfileActivityView, private val sharedPreferences: SharedPreferences) {

    init {
        getUserDefaults()
        getPreviousOrders()
    }

    private fun getUserDefaults() {
        val defaults = HashMap<String, String>()
        defaults.put(Constants.DEF_NAME, sharedPreferences.getString(SignUpActivity.DEF_NAME, ""))
        defaults.put(Constants.DEF_PHONE, sharedPreferences.getString(SignUpActivity.DEF_PHONE, ""))
        defaults.put(Constants.DEF_ADDRESS, sharedPreferences.getString(SignUpActivity.DEF_ADDRESS, ""))

        activityView.displayUserDefaults(defaults)
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
                        saveToSharedPreferences(name, phone, address)
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

    private fun saveToSharedPreferences(name: String, phone: String, address: String) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.DEF_NAME, name)
        editor.putString(Constants.DEF_PHONE, phone)
        editor.putString(Constants.DEF_ADDRESS, address)

        editor.apply()
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        val editor = sharedPreferences.edit()
        editor.remove(Constants.DEF_NAME)
        editor.remove(Constants.DEF_PHONE)
        editor.remove(Constants.DEF_ADDRESS)
        editor.apply()
    }
}
