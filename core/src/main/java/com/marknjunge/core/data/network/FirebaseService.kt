package com.marknjunge.core.data.network

interface FirebaseService {
    suspend fun getFcmToken(): String
}