package com.marknkamau.justjava.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.marknjunge.core.data.network.GoogleSignInClientStub
import kotlinx.coroutines.tasks.await

class GoogleSignInClientStubImpl(private val googleSignInClient: GoogleSignInClient) : GoogleSignInClientStub {
    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }
}