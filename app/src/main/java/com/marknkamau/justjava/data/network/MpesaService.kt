package com.marknkamau.justjava.data.network

import com.marknkamau.justjava.data.models.LNMPaymentResponse
import com.marknkamau.justjava.data.models.OAuthAccess
import com.marknkamau.justjava.data.models.STKPush
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface MpesaService {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    @Headers("cache-control: no-cache")
    fun getAccessToken(@Header("Authorization") authHeader: String): Single<OAuthAccess>

    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(@Header("Authorization") authHeader: String, @Body stkPush: STKPush): Single<LNMPaymentResponse>
}
