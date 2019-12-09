package com.marknjunge.core.payments

import com.marknjunge.core.BuildConfig
import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.model.*
import com.marknjunge.core.utils.Utils

interface PaymentsRepository {
    suspend fun makeLnmoRequest(
        amount: Int,
        phoneNumber: String,
        customerId: String,
        accountRef: String
    ): ApiResponse
}

internal class PaymentsRepositoryImpl : PaymentsRepository {
    private val paymentsService: PaymentsService by lazy {
        NetworkProvider().paymentsService
    }

    override suspend fun makeLnmoRequest(
        amount: Int,
        phoneNumber: String,
        customerId: String,
        accountRef: String
    ): ApiResponse {
        val lnmoRequest = LnmoRequest(
            amount.toString(),
            Utils.sanitizePhoneNumber(phoneNumber),
            customerId,
            accountRef
        )
        return paymentsService.makeRequest(BuildConfig.FunctionsApiKey, lnmoRequest)
    }
}