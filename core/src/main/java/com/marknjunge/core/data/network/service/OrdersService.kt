package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.*
import retrofit2.http.*

internal interface OrdersService {
    @POST("/orders/place")
    suspend fun placeOrder(@Header("session-id") sessionId: String, @Body body: PlaceOrderDto): Order

    @GET("/users/current/orders")
    suspend fun getOrders(@Header("session-id") sessionId: String): List<Order>

    @GET("/orders/{id}")
    suspend fun getOrderById(@Header("session-id") sessionId: String, @Path("id") id: String): Order

    @POST("/orders/{id}/paymentMethod")
    suspend fun changePaymentMethod(
        @Header("session-id") sessionId: String,
        @Path("id") id: String,
        @Body changePaymentMethodDto: ChangePaymentMethodDto
    ): ApiResponse
}
