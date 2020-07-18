package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.VerifyOrderDto
import com.marknjunge.core.data.model.VerifyOrderResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CartService {
    @POST("orders/verify")
    suspend fun verifyCart(@Body body: VerifyOrderDto): List<VerifyOrderResponse>
}
