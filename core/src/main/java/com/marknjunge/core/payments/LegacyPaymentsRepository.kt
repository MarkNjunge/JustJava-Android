package com.marknjunge.core.payments

import com.marknjunge.core.BuildConfig
import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.model.*
import com.marknjunge.core.utils.Utils

interface LegacyPaymentsRepository {
    suspend fun makeLnmoRequest(
        amount: Int,
        phoneNumber: String,
        customerId: String,
        accountRef: String
    ): ApiResponse
}

internal class LegacyPaymentsRepositoryImpl : LegacyPaymentsRepository {
    private val legacyPaymentsService: LegacyPaymentsService by lazy {
        NetworkProvider().legacyPaymentsService
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
        return legacyPaymentsService.makeRequest(BuildConfig.FunctionsApiKey, lnmoRequest)
    }
}