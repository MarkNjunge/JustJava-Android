package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.call
import com.marknjunge.core.data.network.service.PaymentsService

interface PaymentsRepository {
    suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse>

    suspend fun initiateCardPayment(orderId: String, cardDetails: CardDetails): Resource<ApiResponse>
}

internal class ApiPaymentsRepository(private val paymentsService: PaymentsService) : PaymentsRepository {
    override suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse> {
        return call {
            paymentsService.requestMpesa(RequestMpesaDto(mobileNumber, orderId))
        }
    }

    override suspend fun initiateCardPayment(orderId: String, cardDetails: CardDetails): Resource<ApiResponse> {
        return call {
            paymentsService.initiateCardPayment(
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
