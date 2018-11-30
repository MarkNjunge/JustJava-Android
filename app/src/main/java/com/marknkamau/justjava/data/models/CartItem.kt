package com.marknkamau.justjava.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.marknjunge.core.model.OrderItem

@Entity(tableName = "cart")
data class CartItem(@PrimaryKey(autoGenerate = true) val id: Int,
                    var itemName: String,
                    var itemQty: Int,
                    var itemCinnamon: Boolean,
                    var itemChoc: Boolean,
                    var itemMarshmallow: Boolean,
                    var itemPrice: Int) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(itemName)
        writeInt(itemQty)
        writeInt((if (itemCinnamon) 1 else 0))
        writeInt((if (itemChoc) 1 else 0))
        writeInt((if (itemMarshmallow) 1 else 0))
        writeInt(itemPrice)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CartItem> = object : Parcelable.Creator<CartItem> {
            override fun createFromParcel(source: Parcel): CartItem = CartItem(source)
            override fun newArray(size: Int): Array<CartItem?> = arrayOfNulls(size)
        }
    }
}

fun CartItem.toOrderItem(): OrderItem {
    return OrderItem(this.id, this.itemName, this.itemQty, this.itemCinnamon, this.itemChoc, this.itemMarshmallow, this.itemPrice)
}