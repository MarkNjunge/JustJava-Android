package com.marknjunge.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductChoice(
    @SerializedName("id")
	val id: Int,

    @SerializedName("name")
	val name: String,

    @SerializedName("position")
	val position: Int,

    @SerializedName("qtyMax")
	val qtyMax: Int,

    @SerializedName("qtyMin")
	val qtyMin: Int,

    @SerializedName("options")
	val options: List<ProductChoiceOption>

) : Parcelable