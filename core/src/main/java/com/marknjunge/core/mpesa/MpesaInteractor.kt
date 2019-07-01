package com.marknjunge.core.mpesa

import com.marknjunge.core.BuildConfig
import com.marknjunge.core.model.*
import com.marknjunge.core.utils.Utils

interface MpesaInteractor {
    suspend fun makeLnmoRequest(amount: Int,
                            phoneNumber: String,
                            customerId: String,
                            accountRef: String,
                            fcmToken: String
    ): ApiResponse
}

internal class MpesaInteractorImpl : MpesaInteractor {
    private val mpesaService: MpesaService by lazy {
        NetworkProvider().mpesaService
    }

    override suspend fun makeLnmoRequest(amount: Int, phoneNumber: String, customerId: String, accountRef: String, fcmToken: String): ApiResponse {
        val lnmoRequest = LnmoRequest(amount.toString(), Utils.sanitizePhoneNumber(phoneNumber), customerId, accountRef, fcmToken)
        return mpesaService.makeRequest(BuildConfig.FunctionsApiKey, lnmoRequest)
    }
}