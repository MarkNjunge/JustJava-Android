package com.marknjunge.core.data.model

data class CardDetails(
    val cardNo: String,
    val cvv: String,
    val expiryMonth: String,
    val expiryYear: String,
    val billingZip: String,
    val billingCity: String,
    val billingAddress: String,
    val billingState: String,
    val billingCountry: String
)
