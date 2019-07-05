package com.marknkamau.justjava.ui.login

import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.UserService
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

internal class LogInPresenter(private val view: LogInView,
                              private val preferences: PreferencesRepository,
                              private val auth: AuthService,
                              private val userService: UserService,
                              mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    fun checkSignInStatus() {
        if (auth.isSignedIn()) {
            view.closeActivity()
        }
    }

    fun signIn(email: String, password: String) {
        view.showLoading()
        uiScope.launch {
            try {
                val uid = auth.signIn(email, password)
                getUserDefaults(uid)
                setFcmToken()
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message)
            }
        }
    }

    private fun getUserDefaults(id: String) {
        uiScope.launch {
            try {
                val userDetails = userService.getUserDetails(id)

                preferences.saveUserDetails(userDetails)
                view.dismissLoading()
                view.displayMessage("Sign in successful")
                view.finishSignIn()
            } catch (e: Exception) {
                view.displayMessage(e.message)
            }
        }
    }

    private fun setFcmToken() {
        uiScope.launch {
            try {
                userService.updateUserFcmToken(auth.getCurrentUser().userId)
                Timber.i("FCM token saved")
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    fun resetUserPassword(email: String) {
        uiScope.launch {
            try {
                auth.sendPasswordResetEmail(email)
                view.displayMessage("Password reset email sent")
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message)
            }
        }
    }
}
