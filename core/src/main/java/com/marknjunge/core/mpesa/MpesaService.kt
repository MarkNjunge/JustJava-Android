package com.marknjunge.core.mpesa

import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.model.LnmoRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.*

internal interface MpesaService {
    @POST("request")
    suspend fun makeRequest(@Header("ApiKey") apiKeyHeader: String,
                    @Body lnmoRequest: LnmoRequest
    ): ApiResponse
}
