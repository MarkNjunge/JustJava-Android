package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMpesaDto(
    @SerialName("mobileNumber")
    val mobileNumber: String,

    @SerialName("orderId")
    val orderId: String
)
