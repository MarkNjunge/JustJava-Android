package com.marknjunge.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.marknjunge.core.model.AuthUser

interface AuthService {
    fun addStateListener(listener: FirebaseAuth.AuthStateListener)

    suspend fun createUser(email: String, password: String)

    suspend fun signIn(email: String, password: String): String

    suspend fun sendPasswordResetEmail(email: String)

    suspend fun setUserDisplayName(name: String)

    fun getCurrentUser(): AuthUser

    fun isSignedIn() : Boolean

    fun logOut()

    interface AuthActionListener {
        fun actionSuccessful(response: String)

        fun actionFailed(response: String)
    }
}