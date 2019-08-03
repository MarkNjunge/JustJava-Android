package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.mpesa.MpesaInteractor
import io.mockk.coEvery
import io.mockk.mockk

object MockMpesaInteractor {
    fun create(): MpesaInteractor {
        val mock = mockk<MpesaInteractor>()

        coEvery { mock.makeLnmoRequest(any(), any(), any(), any(), any()) } returns ApiResponse("OK")

        return mock
    }
}