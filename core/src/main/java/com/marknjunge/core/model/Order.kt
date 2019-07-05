package com.marknjunge.core.model

import android.os.Parcelable
import com.google.firebase.firestore.FieldValue
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Order(val orderId: String,
                 val customerId: String,
                 val itemsCount: Int,
                 val totalPrice: Int,
                 val deliveryAddress: String,
                 val additionalComments: String,
                 val status: OrderStatus = OrderStatus.PENDING,
                 val date: Date = Date(),
                 val paymentMethod: String,
                 val paymentStatus: String = "unpaid") : Parcelable{
    fun toMap(): MutableMap<String, Any> {
        val orderMap = mutableMapOf<String, Any>()

        orderMap[DatabaseKeys.Order.orderId] = orderId
        orderMap[DatabaseKeys.Order.customerId] = customerId
        orderMap[DatabaseKeys.Order.address] = deliveryAddress
        orderMap[DatabaseKeys.Order.itemsCount] = itemsCount
        orderMap[DatabaseKeys.Order.totalPrice] = totalPrice
        orderMap[DatabaseKeys.Order.status] = status.name
        orderMap[DatabaseKeys.Order.comments] = additionalComments
        orderMap[DatabaseKeys.Order.date] = FieldValue.serverTimestamp()
        orderMap[DatabaseKeys.Order.paymentMethod] = paymentMethod
        orderMap[DatabaseKeys.Order.paymentStatus] = paymentStatus

        return orderMap
    }
}