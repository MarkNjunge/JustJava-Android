package com.marknkamau.justjava.data.models

import android.os.Parcel
import android.os.Parcelable

open class CoffeeDrink(var drinkID: String = "",
                       var drinkName: String = "",
                       var drinkContents: String = "",
                       var drinkDescription: String = "",
                       var drinkPrice: String = "",
                       var drinkImage: String = "")
    : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(drinkID)
        writeString(drinkName)
        writeString(drinkContents)
        writeString(drinkDescription)
        writeString(drinkPrice)
        writeString(drinkImage)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<CoffeeDrink> = object : Parcelable.Creator<CoffeeDrink> {
            override fun createFromParcel(source: Parcel): CoffeeDrink = CoffeeDrink(source)
            override fun newArray(size: Int): Array<CoffeeDrink?> = arrayOfNulls(size)
        }
    }
}
