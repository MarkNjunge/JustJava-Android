package com.marknkamau.justjava.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.marknjunge.core.data.network.GoogleSignInService
import kotlinx.coroutines.tasks.await

class GoogleSignInServiceImpl(private val googleSignInClient: GoogleSignInClient) : GoogleSignInService {
    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }
}
