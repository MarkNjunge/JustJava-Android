package com.marknkamau.justjava.data.network

import com.google.firebase.messaging.FirebaseMessaging
import com.marknjunge.core.data.network.service.FirebaseService
import kotlinx.coroutines.tasks.await

class AppFirebaseService : FirebaseService {
    private val firebaseMessaging = FirebaseMessaging.getInstance()

    override suspend fun getFcmToken(): String = firebaseMessaging.token.await()
}
