package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.service.OrdersService
import com.marknjunge.core.utils.call

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
    override suspend fun placeOrder(dto: PlaceOrderDto): Resource<Order> {
        return call {
            ordersService.placeOrder(preferencesRepository.sessionId, dto)
        }
    }

    override suspend fun getOrders(): Resource<List<Order>> {
        return call {
            ordersService.getOrders(preferencesRepository.sessionId)
        }
    }

    override suspend fun changePaymentMethod(id: String, method: PaymentMethod): Resource<ApiResponse> {
        return call {
            val dto = ChangePaymentMethodDto(method)
            ordersService.changePaymentMethod(preferencesRepository.sessionId, id, dto)
        }
    }

    override suspend fun getOrderById(id: String): Resource<Order> {
        return call {
            ordersService.getOrderById(preferencesRepository.sessionId, id)
        }
    }
}
