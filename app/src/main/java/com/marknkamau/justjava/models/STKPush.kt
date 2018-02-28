package com.marknkamau.justjava.models

import com.google.gson.annotations.SerializedName

data class STKPush(
        @SerializedName("BusinessShortCode")
        private val businessShortCode: String,
        @SerializedName("Password")
        private val password: String,
        @SerializedName("Timestamp")
        private val timestamp: String,
        @SerializedName("TransactionType")
        private val transactionType: String,
        @SerializedName("Amount")
        private val amount: String,
        @SerializedName("PartyA")
        private val partyA: String,
        @SerializedName("PartyB")
        private val partyB: String,
        @SerializedName("PhoneNumber")
        private val phoneNumber: String,
        @SerializedName("CallBackURL")
        private val callBackURL: String,
        @SerializedName("AccountReference")
        private val accountReference: String,
        @SerializedName("TransactionDesc")
        private val transactionDesc: String
)