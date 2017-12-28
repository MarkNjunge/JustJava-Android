package com.marknkamau.justjavastaff.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Order(val orderId: String,
                 val customerName: String,
                 val customerPhone: String,
                 val deliveryAddress: String,
                 val additionalComments: String,
                 val status: String,
                 val timestamp: Date,
                 val totalPrice: Int,
                 val itemsCount: Int) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(orderId)
        writeString(customerName)
        writeString(customerPhone)
        writeString(deliveryAddress)
        writeString(additionalComments)
        writeString(status)
        writeSerializable(timestamp)
        writeInt(totalPrice)
        writeInt(itemsCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
            override fun createFromParcel(source: Parcel): Order = Order(source)
            override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
        }
    }
}
