package com.marknkamau.justjavastaff.ui.login

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val auth: AuthService, private val databaseService: ClientDatabaseService,private val view: LoginView) {

    fun signIn(email: String, password: String) {
        val pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val matcher = pattern.matcher(email)

        if (!matcher.matches()) {
            view.displayMessage("The email is not valid")
            return
        }

        auth.signIn(email, password, object : AuthService.AuthActionListener {
            override fun actionSuccessful(response: String) {
                view.onSignedIn()
                setFcmToken()
            }

            override fun actionFailed(response: String) {
                view.displayMessage(response)
            }
        })
    }

    private fun setFcmToken(){
        databaseService.updateUserFcmToken(auth.getCurrentUser().userId, object : WriteListener {
            override fun onError(reason: String) {
                Timber.e(reason)
            }

            override fun onSuccess() {
                Timber.i("FCM token saved")
            }
        })
    }

}
