package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.payments.LegacyPaymentsRepository
import io.mockk.coEvery
import io.mockk.mockk

object MockMpesaInteractor {
    fun create(): LegacyPaymentsRepository {
        val mock = mockk<LegacyPaymentsRepository>()

        coEvery { mock.makeLnmoRequest(any(), any(), any(), any()) } returns ApiResponse("OK")

        return mock
    }
}