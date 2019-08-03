package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.firebase.PaymentService
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.coEvery
import io.mockk.mockk

object MockPaymentsService {
    fun create(): PaymentService {
        val mock = mockk<PaymentService>()

        coEvery { mock.getPayments() } returns TestData.payments

        return mock
    }
}