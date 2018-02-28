package com.marknkamau.justjava.models

import com.google.gson.annotations.SerializedName

data class LNMPaymentResponse(
        @SerializedName("MerchantRequestID")
        val merchantRequestId: String,
        @SerializedName("ResponseCode")
        val responseCode: String,
        @SerializedName("CustomerMessage")
        val customerMessage: String,
        @SerializedName("CheckoutRequestID")
        val checkoutRequestId: String,
        @SerializedName("ResponseDescription")
        val responseDescription: String
)