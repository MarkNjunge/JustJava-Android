package com.marknjunge.core.data.repository

import com.marknjunge.core.data.api.ApiService
import com.marknjunge.core.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProductsRepository {
    suspend fun getProducts(): List<Product>
}

internal class ApiProductsRepository(private val apiService: ApiService) : ProductsRepository {
    override suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        apiService.getProducts()
    }

}