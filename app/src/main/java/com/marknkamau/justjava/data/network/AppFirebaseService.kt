package com.marknkamau.justjava.data.network

import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.data.network.service.FirebaseService
import kotlinx.coroutines.tasks.await

class AppFirebaseService : FirebaseService {
    private val firebaseInstanceId by lazy { FirebaseInstanceId.getInstance() }

    override suspend fun getFcmToken(): String = firebaseInstanceId.instanceId.await().token
}
