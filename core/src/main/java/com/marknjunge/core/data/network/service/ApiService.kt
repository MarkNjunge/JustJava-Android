package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}
