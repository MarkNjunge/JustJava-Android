package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.InitiateCardPaymentDto
import com.marknjunge.core.data.model.RequestMpesaDto
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.PaymentsService
import com.marknjunge.core.model.ApiResponse
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PaymentsRepository {
    suspend fun requestMpesa(mobileNumber: String, orderId: String): Resource<ApiResponse>

    suspend fun initiateCardPayment(
        orderId: Int,
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
    override suspend fun requestMpesa(mobileNumber: String, orderId: String) = withContext(Dispatchers.IO) {
        call {
            Resource.Success(
                paymentsService.requestMpesa(
                    preferencesRepository.sessionId,
                    RequestMpesaDto(mobileNumber, orderId)
                )
            )
        }
    }

    override suspend fun initiateCardPayment(
        orderId: Int,
        cardNo: String,
        cvv: String,
        expiryMonth: String,
        expiryYear: String,
        billingZip: String,
        billingCity: String,
        billingAddress: String,
        billingState: String,
        billingCountry: String
    ) = withContext(Dispatchers.IO) {
        call {
            Resource.Success(
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
            )
        }
    }

}