package com.marknjunge.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.marknjunge.core.model.AuthUser

internal class AuthServiceImpl : AuthService {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun addStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }

    override fun createUser(email: String, password: String, listener: AuthService.AuthActionListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("Account created") }
                .addOnFailureListener { exception ->
                    listener.actionFailed(exception.message ?: "Unable to create account")
                }
    }

    override fun signIn(email: String, password: String, listener: AuthService.AuthActionListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful(it.user.uid) }
                .addOnFailureListener { exception ->
                    listener.actionFailed(exception.message ?: "Unable to sign in")
                }
    }

    override fun sendPasswordResetEmail(email: String, listener: AuthService.AuthActionListener) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { listener.actionSuccessful("Password reset email sent") }
                .addOnFailureListener { exception ->
                    listener.actionFailed(exception.message ?: "Unable to send email")
                }
    }

    override fun setUserDisplayName(name: String, listener: AuthService.AuthActionListener) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()

        firebaseAuth.currentUser?.updateProfile(profileUpdate)
                ?.addOnSuccessListener { listener.actionSuccessful("User's name set") }
                ?.addOnFailureListener { exception ->
                    listener.actionFailed(exception.message ?: "Unable to update name")
                }
    }

    override fun getCurrentUser(): AuthUser {
        return AuthUser(
                firebaseAuth.currentUser!!.uid,
                firebaseAuth.currentUser!!.email ?: "",
                firebaseAuth.currentUser!!.displayName ?: ""
        )
    }

    override fun isSignedIn() = firebaseAuth.currentUser != null

    override fun logOut() {
        firebaseAuth.signOut()
    }

}
