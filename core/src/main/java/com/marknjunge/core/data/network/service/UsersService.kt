package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.SaveAddressDto
import com.marknjunge.core.data.model.UpdateFcmTokenDto
import com.marknjunge.core.data.model.UpdateUserDto
import retrofit2.http.*

internal interface UsersService {
    @GET("users/current")
    suspend fun getCurrentUser(@Header("session-id") sessionId: String): User

    @PATCH("users/current")
    suspend fun updateUser(@Header("session-id") sessionId: String, @Body body: UpdateUserDto): Unit

    @PATCH("users/current/fcm")
    suspend fun updateFcmToken(@Header("session-id") sessionId: String, @Body body: UpdateFcmTokenDto)

    @POST("users/current/addresses")
    suspend fun saveAddress(@Header("session-id") sessionId: String, @Body body: SaveAddressDto): Address

    @DELETE("users/current/addresses/{id}")
    suspend fun deleteAddress(@Header("session-id") sessionId: String, @Path("id") addressId: Long)

    @DELETE("users/current")
    suspend fun deleteUser(@Header("session-id") sessionId: String)
}
