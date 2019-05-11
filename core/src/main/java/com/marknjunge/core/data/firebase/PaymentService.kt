package com.marknjunge.core.data.firebase

import com.marknjunge.core.model.Payment

interface PaymentService {
    suspend fun getPayments(): List<Payment>
}