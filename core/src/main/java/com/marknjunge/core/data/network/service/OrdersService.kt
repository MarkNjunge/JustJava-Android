package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.*
import retrofit2.http.*

internal interface OrdersService {
    @POST("/orders/place")
    suspend fun placeOrder(@Body body: PlaceOrderDto): Order

    @GET("/users/current/orders")
    suspend fun getOrders(): List<Order>

    @GET("/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Order

    @POST("/orders/{id}/paymentMethod")
    suspend fun changePaymentMethod(
        @Path("id") id: String,
        @Body changePaymentMethodDto: ChangePaymentMethodDto
    ): ApiResponse
}
