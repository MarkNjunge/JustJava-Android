package com.marknjunge.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LnmoRequest(
        @SerialName("amount")
        val amount: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("customerId")
        val customerId: String,
        @SerialName("orderId")
        val orderId: String
)