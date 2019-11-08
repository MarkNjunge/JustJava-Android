package com.marknjunge.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
	val id: String,

    @SerializedName("name")
	val name: String,

    @SerializedName("slug")
	val slug: String,

    @SerializedName("image")
	val image: String,

    @SerializedName("createdAt")
	val createdAt: String,

    @SerializedName("price")
	val price: Double,

    @SerializedName("description")
	val description: String,

    @SerializedName("type")
	val type: String,

    @SerializedName("choices")
	val choices: List<ProductChoice>?,

    @SerializedName("status")
	val status: String
) : Parcelable