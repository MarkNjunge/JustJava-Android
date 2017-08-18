package com.marknkamau.justjava.network

import com.google.firebase.auth.FirebaseAuth
import com.marknkamau.justjava.authentication.AuthenticationService

object MockAuthenticationServiceImpl : AuthenticationService {
    override fun addAuthListener(listener: FirebaseAuth.AuthStateListener) {

    }

    override fun getUserId(): String {
        return ""
    }

    override fun isSignedIn(): Boolean {
        return true
    }

    override fun logOut() {

    }

    override fun signIn(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        listener.actionSuccessful("")
        listener.actionFailed("")
    }

    override fun sendPasswordResetEmail(email: String, listener: AuthenticationService.AuthActionListener) {
        listener.actionSuccessful("")
        listener.actionFailed("")
    }

    override fun createUser(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        listener.actionSuccessful("")
        listener.actionFailed("")
    }

    override fun setUserDisplayName(name: String, listener: AuthenticationService.AuthActionListener) {
        listener.actionSuccessful("")
        listener.actionFailed("")
    }

}
