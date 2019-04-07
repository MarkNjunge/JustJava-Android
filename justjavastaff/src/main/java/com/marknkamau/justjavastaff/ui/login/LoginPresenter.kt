package com.marknkamau.justjavastaff.ui.login

import com.marknjunge.core.auth.AuthService
import java.util.regex.Pattern

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val auth: AuthService, private val view: LoginView) {

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
            }

            override fun actionFailed(response: String) {
                view.displayMessage(response)
            }
        })
    }

}
