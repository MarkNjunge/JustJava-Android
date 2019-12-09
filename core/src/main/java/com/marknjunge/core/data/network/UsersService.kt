package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.SaveAddressDto
import com.marknjunge.core.data.model.UpdateFcmTokenDto
import com.marknjunge.core.data.model.UpdateUserDto
import retrofit2.http.*

internal interface UsersService {
    @GET("users/current")
    suspend fun getCurrentUser(): User

    @PATCH("users/current")
    suspend fun updateUser(@Body body: UpdateUserDto): Unit

    @PATCH("users/current")
    suspend fun updateFcmToken(@Body body: UpdateFcmTokenDto)

    @POST("users/current/addresses")
    suspend fun saveAddress(@Body body: SaveAddressDto): Address

    @DELETE("users/current/addresses/{id}")
    suspend fun deleteAddress(@Path("id") addressId: Long)
}