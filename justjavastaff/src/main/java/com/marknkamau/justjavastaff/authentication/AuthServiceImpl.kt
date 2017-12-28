package com.marknkamau.justjavastaff.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marknkamau.justjavastaff.models.Employee

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class AuthServiceImpl : AuthenticationService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signIn(email: String, password: String, listener: AuthenticationService.AuthListener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult -> listener.onSuccess(getEmployeeFromUser(authResult.user)) }
                .addOnFailureListener { e -> listener.onError(e.message ?: "Error signing in") }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun currentEmployee(): Employee? {
        val currentUser = auth.currentUser
        return if (currentUser == null) {
            null
        } else {
            getEmployeeFromUser(currentUser)
        }
    }

    private fun getEmployeeFromUser(user: FirebaseUser): Employee {
        //TODO Admin interface should create users with a display name.
        val name = user.email!!.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

        return Employee(user.uid, name, user.email!!)
    }
}
