package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.ApiService
import com.marknjunge.core.data.model.Product
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProductsRepository {
    suspend fun getProducts(): Resource<List<Product>>
}

internal class ApiProductsRepository(private val apiService: ApiService) : ProductsRepository {
    override suspend fun getProducts(): Resource<List<Product>> = withContext(Dispatchers.IO) {
        call {
            Resource.Success(apiService.getProducts())
        }
    }
}