package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.InitiateCardPaymentDto
import com.marknjunge.core.data.model.RequestMpesaDto
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.service.PaymentsService
import com.marknjunge.core.utils.call

interface PaymentsRepository {
    suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse>

    suspend fun initiateCardPayment(
        orderId: String,
        cardNo: String,
        cvv: String,
        expiryMonth: String,
        expiryYear: String,
        billingZip: String,
        billingCity: String,
        billingAddress: String,
        billingState: String,
        billingCountry: String
    ): Resource<ApiResponse>
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

    override suspend fun initiateCardPayment(
        orderId: String,
        cardNo: String,
        cvv: String,
        expiryMonth: String,
        expiryYear: String,
        billingZip: String,
        billingCity: String,
        billingAddress: String,
        billingState: String,
        billingCountry: String
    ): Resource<ApiResponse> {
        return call {
            paymentsService.initiateCardPayment(
                preferencesRepository.sessionId,
                InitiateCardPaymentDto(
                    orderId,
                    cardNo,
                    cvv,
                    expiryMonth,
                    expiryYear,
                    billingZip,
                    billingCity,
                    billingAddress,
                    billingState,
                    billingCountry
                )
            )
        }
    }

}