package com.marknkamau.justjava.ui.login

import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.model.UserDetails
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class LogInPresenter(private val activityView: LogInView,
                              private val preferences: PreferencesRepository,
                              private val auth: AuthService,
                              private val database: ClientDatabaseService,
                              mainDispatcher: CoroutineDispatcher) {

    private val job = Job()
    private val uiScope = CoroutineScope(job + mainDispatcher)

    fun cancel() {
        job.cancel()
    }

    fun checkSignInStatus() {
        if (auth.isSignedIn()) {
            activityView.closeActivity()
        }
    }

    fun signIn(email: String, password: String) {
        activityView.showDialog()
        uiScope.launch {
            try {
                val uid = auth.signIn(email, password)
                getUserDefaults(uid)
                setFcmToken()
            } catch (e: Exception) {
                Timber.e(e)
                activityView.displayMessage(e.message)
            }
        }
    }

    private fun getUserDefaults(id: String) {
        database.getUserDefaults(id, object : ClientDatabaseService.UserDetailsListener {
            override fun onSuccess(userDetails: UserDetails) {
                preferences.saveUserDetails(userDetails)
                activityView.dismissDialog()
                activityView.displayMessage("Sign in successful")
                activityView.finishSignIn()
            }

            override fun onError(reason: String) {
                activityView.displayMessage(reason)
            }
        })
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


    fun resetUserPassword(email: String) {
        uiScope.launch {
            try {
                auth.sendPasswordResetEmail(email)
                activityView.displayMessage("Password reset email sent")
            } catch (e: Exception) {
                Timber.e(e)
                activityView.displayMessage(e.message)
            }
        }
    }
}
