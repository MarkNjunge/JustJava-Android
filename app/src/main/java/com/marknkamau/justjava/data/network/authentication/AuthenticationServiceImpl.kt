package com.marknkamau.justjava.data.network.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

object AuthenticationServiceImpl : AuthenticationService {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var isSignedIn = false

    init {
        firebaseAuth.addAuthStateListener {
            isSignedIn = it.currentUser != null
        }
    }

    override fun addAuthListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }

    override fun createUser(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("User created successfully") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message ?: "Unable to create account") }
    }

    override fun signIn(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful(it.user.uid) }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message ?: "Unable to sign in") }
    }

    override fun sendPasswordResetEmail(email: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { listener.actionSuccessful("Password reset email sent") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message ?: "Unable to send email") }
    }

    override fun setUserDisplayName(name: String, listener: AuthenticationService.AuthActionListener) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()

        firebaseAuth.currentUser?.updateProfile(profileUpdate)
                ?.addOnSuccessListener { listener.actionSuccessful("User display name set") }
                ?.addOnFailureListener { exception -> listener.actionFailed(exception.message ?: "Unable to update profile") }
    }

    override fun getUserId() = if (isSignedIn) firebaseAuth.currentUser!!.uid else null

    override fun isSignedIn() = isSignedIn

    override fun logOut() {
        firebaseAuth.signOut()
    }

}
