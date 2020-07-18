package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.VerifyOrderDto
import com.marknjunge.core.data.model.VerifyOrderResponse
import com.marknjunge.core.data.network.call
import com.marknjunge.core.data.network.service.CartService

interface CartRepository {
    suspend fun verifyOrder(dto: VerifyOrderDto): Resource<List<VerifyOrderResponse>>
}

internal class ApiCartRepository(private val cartService: CartService) : CartRepository {
    override suspend fun verifyOrder(dto: VerifyOrderDto): Resource<List<VerifyOrderResponse>> {
        return call {
            cartService.verifyCart(dto)
        }
    }
}
