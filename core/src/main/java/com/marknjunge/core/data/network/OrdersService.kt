package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.PlaceOrderDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface OrdersService{
    @POST("/orders/place")
    suspend fun placeOrder(@Body body: PlaceOrderDto): Order

    @GET("/users/current/orders")
    suspend fun getOrders(): List<Order>

    @GET("/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Order
}