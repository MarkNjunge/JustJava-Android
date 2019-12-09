package com.marknjunge.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ProductChoice(
    @SerialName("id")
	val id: Int,

    @SerialName("name")
	val name: String,

    @SerialName("position")
	val position: Int,

    @SerialName("qtyMax")
	val qtyMax: Int,

    @SerialName("qtyMin")
	val qtyMin: Int,

    @SerialName("options")
	val options: List<ProductChoiceOption>

) : Parcelable