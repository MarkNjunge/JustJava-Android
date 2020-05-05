package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePaymentMethodDto(
    @SerialName("paymentMethod")
    val paymentMethod: PaymentMethod
)
