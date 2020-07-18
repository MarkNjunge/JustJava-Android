package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.service.ApiService
import com.marknjunge.core.data.network.call

interface ProductsRepository {
    suspend fun getProducts(): Resource<List<Product>>
}

internal class ApiProductsRepository(private val apiService: ApiService) : ProductsRepository {
    override suspend fun getProducts(): Resource<List<Product>> {
        return call {
            apiService.getProducts()
        }
    }
}
