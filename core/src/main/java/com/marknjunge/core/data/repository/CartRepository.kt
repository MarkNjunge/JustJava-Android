package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.CartService
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CartRepository {
    suspend fun verifyOrder(dto: VerifyOrderDto): Resource<List<VerifyOrderResponse>>
}

internal class ApiCartRepository(private val cartService: CartService) : CartRepository {
    override suspend fun verifyOrder(dto: VerifyOrderDto) = withContext(Dispatchers.IO) {
        call {
            Resource.Success(cartService.verifyCart(dto))
        }
    }
}