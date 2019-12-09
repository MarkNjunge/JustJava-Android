package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.UpdateFcmTokenDto
import com.marknjunge.core.data.model.UpdateUserDto
import com.marknjunge.core.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

internal interface UsersService {
    @GET("users/current")
    suspend fun getCurrentUser(): User

    @PATCH("users/current")
    suspend fun updateUser(@Body body: UpdateUserDto): Unit

    @PATCH("users/current")
    suspend fun updateFcmToken(@Body body: UpdateFcmTokenDto)
}