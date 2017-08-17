package com.marknkamau.justjava.models

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CartItem(@PrimaryKey var itemID: Int = 0,
                    var itemName: String = "",
                    var itemQty: String = "",
                    var itemCinnamon: String = "",
                    var itemChoc: String = "",
                    var itemMarshmallow: String = "",
                    var itemPrice: Int = 0)
    : RealmObject(), Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(itemID)
        writeString(itemName)
        writeString(itemQty)
        writeString(itemCinnamon)
        writeString(itemChoc)
        writeString(itemMarshmallow)
        writeInt(itemPrice)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<CartItem> = object : Parcelable.Creator<CartItem> {
            override fun createFromParcel(source: Parcel): CartItem = CartItem(source)
            override fun newArray(size: Int): Array<CartItem?> = arrayOfNulls(size)
        }
    }
}
