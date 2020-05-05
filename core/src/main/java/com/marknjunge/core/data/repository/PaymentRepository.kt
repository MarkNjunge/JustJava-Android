package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.service.PaymentsService
import com.marknjunge.core.utils.call

interface PaymentsRepository {
    suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse>

    suspend fun initiateCardPayment(orderId: String, cardDetails: CardDetails): Resource<ApiResponse>
}

internal class ApiPaymentsRepository(
    private val paymentsService: PaymentsService,
    private val preferencesRepository: PreferencesRepository
) : PaymentsRepository {
    override suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse> {
        return call {
            paymentsService.requestMpesa(preferencesRepository.sessionId, RequestMpesaDto(mobileNumber, orderId))
        }
    }

    override suspend fun initiateCardPayment(orderId: String, cardDetails: CardDetails): Resource<ApiResponse> {
        return call {
            paymentsService.initiateCardPayment(
                preferencesRepository.sessionId,
                InitiateCardPaymentDto(
                    orderId,
                    cardDetails.cardNo,
                    cardDetails.cvv,
                    cardDetails.expiryMonth,
                    cardDetails.expiryYear,
                    cardDetails.billingZip,
                    cardDetails.billingCity,
                    cardDetails.billingAddress,
                    cardDetails.billingState,
                    cardDetails.billingCountry
                )
            )
        }
    }
}
