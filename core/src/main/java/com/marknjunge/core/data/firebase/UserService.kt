package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.UserDetails

interface UserService {
    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails(id: String, name: String, phone: String, address: String)

    suspend fun updateUserFcmToken(userId: String)

    suspend fun getUserDetails(id: String): UserDetails
}