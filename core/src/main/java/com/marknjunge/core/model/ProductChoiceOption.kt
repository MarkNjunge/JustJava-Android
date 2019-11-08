package com.marknjunge.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductChoiceOption(
	@SerializedName("price")
	val price: Double,

	@SerializedName("name")
	val name: String,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("id")
	val id: Int
) : Parcelable