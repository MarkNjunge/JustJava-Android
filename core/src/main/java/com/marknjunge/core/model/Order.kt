package com.marknjunge.core.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Order(val orderId: String,
                 val customerId: String,
                 var itemsCount: Int,
                 var totalPrice: Int,
                 val deliveryAddress: String,
                 val additionalComments: String,
                 val status: OrderStatus = OrderStatus.PENDING,
                 val date: Date = Date(),
                 val paymentMethod: String,
                 val paymentStatus: String = "unpaid") : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            OrderStatus.values()[source.readInt()],
            source.readSerializable() as Date,
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(orderId)
        writeString(customerId)
        writeInt(itemsCount)
        writeInt(totalPrice)
        writeString(deliveryAddress)
        writeString(additionalComments)
        writeInt(status.ordinal)
        writeSerializable(date)
        writeString(paymentMethod)
        writeString(paymentStatus)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
            override fun createFromParcel(source: Parcel): Order = Order(source)
            override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
        }
    }
}
