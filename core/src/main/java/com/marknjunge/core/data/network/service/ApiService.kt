package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.Product
import retrofit2.http.GET

internal interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}
