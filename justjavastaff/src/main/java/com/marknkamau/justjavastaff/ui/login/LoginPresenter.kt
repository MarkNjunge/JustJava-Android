package com.marknkamau.justjavastaff.ui.login

import com.marknjunge.core.auth.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val auth: AuthService, private val view: LoginView, mainDispatcher: CoroutineDispatcher) {

    private val job = Job()
    private val uiScope = CoroutineScope(job + mainDispatcher)

    fun signIn(email: String, password: String) {
        val pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val matcher = pattern.matcher(email)

        if (!matcher.matches()) {
            view.displayMessage("The email is not valid")
            return
        }

        uiScope.launch {
            try {
                auth.signIn(email, password)
                view.onSignedIn()
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message ?: "Unable to sign in")
            }
        }
    }

    fun cancel() {
        job.cancel()
    }

}
