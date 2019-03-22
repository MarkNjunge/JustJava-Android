package com.marknjunge.core.model

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object DatabaseKeys {
    object User {
        const val userId = "id"
        const val name = "name"
        const val email = "email"
        const val phone = "phone"
        const val address = "address"
        const val fcmToken = "fcmToken"
    }

    object Order {
        const val orderId = "orderId"
        const val customerId = "customerId"
        const val address = "address"
        const val itemsCount = "itemsCount"
        const val totalPrice = "totalPrice"
        const val status = "status"
        const val comments = "comments"
        const val date = "date"
        const val paymentMethod = "paymentMethod"
        const val paymentStatus = "paymentStatus"
        const val fcmToken = "fcmToken"
    }

    object OrderItem {
        const val itemName = "itemName"
        const val itemQty = "itemQty"
        const val itemCinnamon = "itemCinnamon"
        const val itemChoc = "itemChoc"
        const val itemMarshmallow = "itemMarshmallow"
        const val itemPrice = "itemPrice"
    }

    object Payment {
        const val checkoutRequestId = "checkoutRequestId"
        const val merchantRequestId = "merchantRequestId"
        const val orderId = "orderId"
        const val customerId = "customerId"
        const val resultDesc = "resultDesc"
        const val date = "date"
        const val status = "status"
        const val mpesaReceiptNumber = "mpesaReceiptNumber"
        const val phoneNumber = "phoneNumber"
    }
}