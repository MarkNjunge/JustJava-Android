package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.OrdersService
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface OrdersRepository {
    suspend fun placeOrder(dto: PlaceOrderDto): Resource<Order>

    suspend fun getOrders(): Resource<List<Order>>

    suspend fun changePaymentMethod(id: String, method: PaymentMethod): Resource<ApiResponse>

    suspend fun getOrderById(id: String): Resource<Order>
}

internal class ApiOrdersRepository(
    private val ordersService: OrdersService,
    private val preferencesRepository: PreferencesRepository
) : OrdersRepository {
    override suspend fun placeOrder(dto: PlaceOrderDto) = withContext(Dispatchers.IO) {
        call {
            Resource.Success(ordersService.placeOrder(preferencesRepository.sessionId, dto))
        }
    }

    override suspend fun getOrders(): Resource<List<Order>> = withContext(Dispatchers.IO) {
        call {
            Resource.Success(ordersService.getOrders(preferencesRepository.sessionId))
        }
    }

    override suspend fun changePaymentMethod(id: String, method: PaymentMethod) = withContext(Dispatchers.IO) {
        call {
            val dto = ChangePaymentMethodDto(method)
            val response = ordersService.changePaymentMethod(preferencesRepository.sessionId, id, dto)
            Resource.Success(response)
        }
    }

    override suspend fun getOrderById(id: String): Resource<Order> = withContext(Dispatchers.IO) {
        call {
            Resource.Success(ordersService.getOrderById(preferencesRepository.sessionId, id))
        }
    }
}