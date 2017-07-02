package com.marknkamau.justjava.ui.signup

import android.os.Handler
import com.marknkamau.justjava.data.PreferencesRepository

import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.utils.FirebaseAuthUtils
import com.marknkamau.justjava.utils.FirebaseDBUtil

internal class SignUpActivityPresenter(private val activityView: SignUpActivityView, private val preferences: PreferencesRepository) {

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        FirebaseAuthUtils.createUser(email, password, object : FirebaseAuthUtils.AuthActionListener {
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
        FirebaseAuthUtils.signIn(email, password, object : FirebaseAuthUtils.AuthActionListener {
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
        FirebaseAuthUtils.setUserDisplayName(name, object : FirebaseAuthUtils.AuthActionListener {
            override fun actionSuccessful(response: String) {
                FirebaseDBUtil.setUserDefaults(UserDefaults(name, phone, address), object : FirebaseDBUtil.UploadListener {
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
