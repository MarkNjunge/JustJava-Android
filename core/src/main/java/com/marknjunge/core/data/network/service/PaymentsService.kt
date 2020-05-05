package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.InitiateCardPaymentDto
import com.marknjunge.core.data.model.RequestMpesaDto
import com.marknjunge.core.data.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentsService {
    @POST("/payments/mpesa/request")
    suspend fun requestMpesa(
        @Header("session-id") sessionId: String,
        @Body body: RequestMpesaDto
    ): ApiResponse

    @POST("/payments/card/initiate")
    suspend fun initiateCardPayment(
        @Header("session-id") sessionId: String,
        @Body body: InitiateCardPaymentDto
    ): ApiResponse
}
