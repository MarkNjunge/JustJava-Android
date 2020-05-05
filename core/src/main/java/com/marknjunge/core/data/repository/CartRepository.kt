package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.VerifyOrderDto
import com.marknjunge.core.data.model.VerifyOrderResponse
import com.marknjunge.core.data.network.service.CartService
import com.marknjunge.core.utils.call

interface CartRepository {
    suspend fun verifyOrder(dto: VerifyOrderDto): Resource<List<VerifyOrderResponse>>
}

internal class ApiCartRepository(
    private val cartService: CartService,
    private val preferencesRepository: PreferencesRepository
) : CartRepository {
    override suspend fun verifyOrder(dto: VerifyOrderDto): Resource<List<VerifyOrderResponse>> {
        return call {
            cartService.verifyCart(preferencesRepository.sessionId, dto)
        }
    }
}
