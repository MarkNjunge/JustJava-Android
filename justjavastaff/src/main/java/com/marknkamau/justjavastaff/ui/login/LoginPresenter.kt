package com.marknkamau.justjavastaff.ui.login

import com.marknkamau.justjavastaff.authentication.AuthenticationService
import com.marknkamau.justjavastaff.models.Employee

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter(private val auth: AuthenticationService, private val view: LoginView) {

    fun signIn(email: String, password: String) {
        val pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val matcher = pattern.matcher(email)

        if (!matcher.matches()) {
            view.displayMessage("The email is not valid")
            return
        }

        auth.signIn(email, password, object : AuthenticationService.AuthListener {
            override fun onSuccess(employee: Employee) {
                view.onSignedIn()
            }

            override fun onError(reason: String) {
                view.displayMessage(reason)
            }
        })
    }

}
