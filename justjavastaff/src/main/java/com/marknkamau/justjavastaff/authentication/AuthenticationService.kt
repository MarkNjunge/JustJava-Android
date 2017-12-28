package com.marknkamau.justjavastaff.authentication

import com.marknkamau.justjavastaff.models.Employee

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface AuthenticationService {
    fun signIn(email: String, password: String, listener: AuthListener)

    fun signOut()

    fun currentEmployee(): Employee?

    interface AuthListener {
        fun onSuccess(employee: Employee)

        fun onError(reason: String)
    }
}
