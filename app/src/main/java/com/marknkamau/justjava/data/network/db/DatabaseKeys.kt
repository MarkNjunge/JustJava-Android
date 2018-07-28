package com.marknkamau.justjava.data.network.db

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object DatabaseKeys{
    object Order{
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
    }

    object OrderItem{
        const val itemName = "itemName"
        const val itemQty = "itemQty"
        const val itemCinnamon = "itemCinnamon"
        const val itemChoc = "itemChoc"
        const val itemMarshmallow = "itemMarshmallow"
        const val itemPrice = "itemPrice"
    }

    object Payment{
        const val CHECKOUT_REQUEST_ID = "checkoutRequestId"
        const val MERCHANT_REQUEST_ID = "merchantRequestId"
        const val ORDER_ID = "orderId"
        const val CUSTOMER_ID = "customerId"
        const val STATUS = "status"
    }
}