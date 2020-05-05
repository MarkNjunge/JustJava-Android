package com.marknjunge.core.data.model

import com.marknjunge.core.utils.serialization.PaymentMethodSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PaymentMethodSerializer::class)
enum class PaymentMethod(val s: String) {
    CASH("CASH"),
    MPESA("MPESA"),
    CARD("CARD")
}
