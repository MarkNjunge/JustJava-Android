package com.marknkamau.justjava.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object AuthenticationServiceImpl : AuthenticationService {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var user: FirebaseUser? = firebaseAuth.currentUser

    override fun addAuthListener(listener: FirebaseAuth.AuthStateListener){
        firebaseAuth.addAuthStateListener(listener)
    }

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun logOut() {
        firebaseAuth.signOut()
        user = AuthenticationServiceImpl.firebaseAuth.currentUser
    }

    override fun signIn(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("Sign in successful") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    override fun sendPasswordResetEmail(email: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { listener.actionSuccessful("Password reset email sent") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    override fun createUser(email: String, password: String, listener: AuthenticationService.AuthActionListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { listener.actionSuccessful("User created successfully") }
                .addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    override fun setUserDisplayName(name: String, listener: AuthenticationService.AuthActionListener) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()

        user?.updateProfile(profileUpdate)
                ?.addOnSuccessListener { listener.actionSuccessful("User display name set") }
                ?.addOnFailureListener { exception -> listener.actionFailed(exception.message) }
    }

    override fun getUserId(): String? {
        return user?.uid
    }

    override fun isSignedIn(): Boolean {
        return user != null
    }

}
