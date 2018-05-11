package com.marknkamau.justjava.authentication

import com.google.firebase.auth.FirebaseAuth
import com.marknkamau.justjava.models.UserDetails

interface AuthenticationService {
    fun addAuthListener(listener: FirebaseAuth.AuthStateListener)

    fun createUser(email: String, password: String, listener: AuthActionListener?)

    fun signIn(email: String, password: String, listener: AuthActionListener?)

    fun sendPasswordResetEmail(email: String, listener: AuthActionListener?)

    fun setUserDisplayName(name: String, listener: AuthActionListener?)

    fun getUserId(): String?

    fun isSignedIn() : Boolean

    fun logOut()

    interface AuthActionListener {
        fun actionSuccessful(response: String)

        fun actionFailed(response: String?)
    }
}