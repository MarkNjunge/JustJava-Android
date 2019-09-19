package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.payments.PaymentsRepository
import io.mockk.coEvery
import io.mockk.mockk

object MockMpesaInteractor {
    fun create(): PaymentsRepository {
        val mock = mockk<PaymentsRepository>()

        coEvery { mock.makeLnmoRequest(any(), any(), any(), any()) } returns ApiResponse("OK")

        return mock
    }
}