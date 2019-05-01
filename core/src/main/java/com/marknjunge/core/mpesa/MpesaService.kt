package com.marknjunge.core.mpesa

import com.marknjunge.core.model.LNMPaymentResponse
import com.marknjunge.core.model.OAuthAccess
import com.marknjunge.core.model.STKPush
import kotlinx.coroutines.Deferred
import retrofit2.http.*

internal interface MpesaService {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    @Headers("cache-control: no-cache")
    fun getAccessToken(@Header("Authorization") authHeader: String): Deferred<OAuthAccess>

    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(@Header("Authorization") authHeader: String, @Body stkPush: STKPush): Deferred<LNMPaymentResponse>
}
