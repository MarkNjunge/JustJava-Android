package com.marknjunge.core.data.network

import com.marknjunge.core.model.Product
import retrofit2.http.GET

internal interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}