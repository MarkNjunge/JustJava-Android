package com.marknjunge.core.payments

import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.model.LnmoRequest
import retrofit2.http.*

internal interface LegacyPaymentsService {
    @POST("mpesa/request")
    suspend fun makeRequest(
        @Header("x-api-key") apiKeyHeader: String,
        @Body lnmoRequest: LnmoRequest
    ): ApiResponse
}
