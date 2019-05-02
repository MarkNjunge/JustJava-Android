package com.marknjunge.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoffeeDrink(var drinkID: String = "",
                       var drinkName: String = "",
                       var drinkContents: String = "",
                       var drinkDescription: String = "",
                       var drinkPrice: String = "",
                       var drinkImage: String = ""
) : Parcelable