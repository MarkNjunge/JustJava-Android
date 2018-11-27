package com.marknjunge.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.marknjunge.core.model.AuthUser

interface AuthService {
    fun addStateListener(listener: FirebaseAuth.AuthStateListener)

    fun createUser(email: String, password: String, listener: AuthActionListener)

    fun signIn(email: String, password: String, listener: AuthActionListener)

    fun sendPasswordResetEmail(email: String, listener: AuthActionListener)

    fun setUserDisplayName(name: String, listener: AuthActionListener)

    fun getCurrentUser(): AuthUser

    fun isSignedIn() : Boolean

    fun logOut()

    interface AuthActionListener {
        fun actionSuccessful(response: String)

        fun actionFailed(response: String)
    }
}