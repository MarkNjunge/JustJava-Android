package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.firebase.OrderService
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

object MockOrderService {
    fun create(): OrderService {
        val mock = mockk<OrderService>()

        coEvery { mock.placeNewOrder(any(), any()) } just runs
        coEvery { mock.getPreviousOrders(any()) } returns TestData.orders
        coEvery { mock.getOrderItems(any()) } returns TestData.orderItems
        coEvery { mock.getOrder(any()) } returns TestData.order
        coEvery { mock.getAllOrders() } returns TestData.orders
        coEvery { mock.updateOrderStatus(any(), any()) } just runs

        return mock
    }
}