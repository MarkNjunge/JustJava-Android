package com.marknkamau.justjava.ui.signup

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.model.UserDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class SignUpPresenter(private val activityView: SignUpView,
                               private val preferences: PreferencesRepository,
                               private val auth: AuthService,
                               private val database: ClientDatabaseService,
                               mainDispatcher: CoroutineDispatcher) {

    private val job = Job()
    private val uiScope = CoroutineScope(job + mainDispatcher)

    fun createUser(email: String, password: String, name: String, phone: String, address: String) {
        activityView.disableUserInteraction()

        uiScope.launch {
            try {
                auth.createUser(email, password)
                signInUser(email, password, name, phone, address)
            } catch (e: Exception) {
                Timber.e(e)
                activityView.enableUserInteraction()
                activityView.displayMessage(e.message ?: "Unable to sign up")
            }
        }
    }

    private fun signInUser(email: String, password: String, name: String, phone: String, address: String) {
        uiScope.launch {
            try {
                val uid = auth.signIn(email, password)
                setUserDisplayName(uid, email, name, phone, address)
            } catch (e: Exception) {
                Timber.e(e)
                activityView.enableUserInteraction()
                activityView.displayMessage(e.message ?: "Unable to sign in")
            }
        }
    }

    private fun setUserDisplayName(id: String, email: String, name: String, phone: String, address: String) {
        uiScope.launch {
            try {
                auth.setUserDisplayName(name)
                val userDetails = UserDetails(id, email, name, phone, address)

                database.saveUserDetails(userDetails, object : WriteListener {
                    override fun onSuccess() {
                        activityView.enableUserInteraction()
                        preferences.saveUserDetails(userDetails)
                        activityView.displayMessage("Sign up successfully")
                        activityView.finishActivity()
                        setFcmToken()
                    }

                    override fun onError(reason: String) {
                        activityView.enableUserInteraction()
                        activityView.displayMessage(reason)
                    }
                })
            } catch (e: Exception) {
                Timber.e(e)
                activityView.enableUserInteraction()
                activityView.displayMessage(e.message ?: "Unable to create account")
            }
        }
    }

    private fun setFcmToken() {
        database.updateUserFcmToken(auth.getCurrentUser().userId, object : WriteListener {
            override fun onError(reason: String) {
                Timber.e(reason)
            }

            override fun onSuccess() {
                Timber.i("FCM token saved")
            }
        })
    }
}
