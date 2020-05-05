package com.marknjunge.core.data.network.service

interface FirebaseService {
    suspend fun getFcmToken(): String
}
