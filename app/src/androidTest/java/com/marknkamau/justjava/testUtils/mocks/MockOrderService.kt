package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

object MockOrderService {
    val orderPairs = mutableListOf<Pair<Order, List<OrderItem>>>()

    fun create(): OrderService {
        val mock = mockk<OrderService>()

        orderPairs.add(Pair(TestData.order, TestData.orderItems))

        coEvery {
            mock.placeNewOrder(any(), any())
        } answers {
            orderPairs.add(Pair(firstArg(), secondArg()))
        }
        coEvery { mock.getPreviousOrders(any()) } returns orderPairs.map { it.first }
        coEvery {
            mock.getOrderItems(any())
        } answers {
            orderPairs.first { it.first.orderId == firstArg() }.second
        }
        coEvery { mock.getOrder(any()) } answers {
            (orderPairs.first { it.first.orderId == firstArg() }).first
        }
        coEvery { mock.getAllOrders() } returns orderPairs.map { it.first }
        coEvery { mock.updateOrderStatus(any(), any()) } just runs

        return mock
    }
}