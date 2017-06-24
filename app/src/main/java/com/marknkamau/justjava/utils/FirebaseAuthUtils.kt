package com.marknkamau.justjava.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object FirebaseAuthUtils {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun signIn(email: String, password: String, listener: AuthActionListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("Sign in successful") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    fun sendPasswordResetEmail(email: String, listener: AuthActionListener) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { listener.actionSuccessful("Password reset email sent") }.
                addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    fun createUser(email: String, password: String, listener: AuthActionListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("User created successfully") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    fun setUserDisplayName(name: String, listener: AuthActionListener) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        currentUser!!.updateProfile(profileUpdate)
                .addOnSuccessListener { listener.actionSuccessful("User display name set") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    interface AuthActionListener {
        fun actionSuccessful(response: String)

        fun actionFailed(response: String?)
    }

}
