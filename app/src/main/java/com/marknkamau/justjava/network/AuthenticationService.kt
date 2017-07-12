package com.marknkamau.justjava.network

interface AuthenticationService {
    fun logOut()

    fun signIn(email: String, password: String, listener: AuthActionListener)

    fun sendPasswordResetEmail(email: String, listener: AuthActionListener)

    fun createUser(email: String, password: String, listener: AuthActionListener)

    fun setUserDisplayName(name: String, listener: AuthActionListener)

    fun getUserId() : String

    fun isSignedIn() : Boolean

    interface AuthActionListener {
        fun actionSuccessful(response: String?)

        fun actionFailed(response: String?)
    }
}