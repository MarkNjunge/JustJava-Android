package com.marknkamau.justjava.data.models

enum class NotificationReason(val s: String) {
    PAYMENT_COMPLETED("PAYMENT_COMPLETED"),
    PAYMENT_CANCELLED("PAYMENT_CANCELLED"),
    ORDER_STATUS_UPDATED("ORDER_STATUS_UPDATED"),
}
