package com.marknjunge.core.model

data class Payment(val checkoutRequestId: String,
                   val customerId: String,
                   val date: Long,
                   val merchantRequestId: String,
                   val orderId: String,
                   val resultDesc: String,
                   val status: String,
                   val mpesaReceiptNumber: String?,
                   val phoneNumber: String?)