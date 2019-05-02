package com.marknjunge.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.marknjunge.core.model.AuthUser
import kotlinx.coroutines.tasks.await

internal class AuthServiceImpl : AuthService {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun addStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }

    override suspend fun createUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signIn(email: String, password: String): String {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user.uid
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override suspend fun setUserDisplayName(name: String) {
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        firebaseAuth.currentUser?.updateProfile(profileUpdate)?.await()
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
