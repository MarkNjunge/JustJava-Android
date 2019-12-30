package com.marknkamau.justjava.data.network

import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.tasks.await

interface FirebaseService {
    suspend fun getFcmToken(): String
}

class AppFirebaseService : FirebaseService {
    private val firebaseInstanceId by lazy { FirebaseInstanceId.getInstance() }

    override suspend fun getFcmToken(): String = firebaseInstanceId.instanceId.await().token

}