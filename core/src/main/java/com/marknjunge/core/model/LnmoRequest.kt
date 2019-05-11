package com.marknjunge.core.model

import com.google.gson.annotations.SerializedName

data class LnmoRequest(
        @SerializedName("amount")
        val amount: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("customerId")
        val customerId: String,
        @SerializedName("orderId")
        val orderId: String,
        @SerializedName("fcmToken")
        val fcmToken: String
)