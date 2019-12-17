package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.PlaceOrderDto
import retrofit2.http.Body
import retrofit2.http.POST

internal interface OrdersService{
    @POST("/orders/place")
    suspend fun placeOrder(@Body body: PlaceOrderDto): Order
}