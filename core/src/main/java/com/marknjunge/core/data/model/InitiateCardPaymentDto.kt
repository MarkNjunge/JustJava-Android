package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateCardPaymentDto(
    @SerialName("orderId")
    var orderId: String,

    @SerialName("cardNo")
    var cardNo: String,

    @SerialName("cvv")
    var cvv: String,

    @SerialName("expiryMonth")
    var expiryMonth: String,

    @SerialName("expiryYear")
    var expiryYear: String,

    @SerialName("billingZip")
    var billingZip: String,

    @SerialName("billingCity")
    var billingCity: String,

    @SerialName("billingAddress")
    var billingAddress: String,

    @SerialName("billingState")
    var billingState: String,

    @SerialName("billingCountry")
    var billingCountry: String
)
