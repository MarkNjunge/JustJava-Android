package com.marknjunge.core.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ProductChoiceOption(
    @SerialName("id")
	   val id: Int,
    @SerialName("price")
	   val price: Double,

    @SerialName("name")
	   val name: String,

    @SerialName("description")
	   val description: String? = null
) : Parcelable
