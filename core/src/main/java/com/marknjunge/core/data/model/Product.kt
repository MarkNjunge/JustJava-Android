package com.marknjunge.core.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    @SerialName("id")
	val id: Long,

    @SerialName("name")
	val name: String,

    @SerialName("slug")
	val slug: String,

    @SerialName("image")
	val image: String,

    @SerialName("createdAt")
	val createdAt: Long,

    @SerialName("price")
	val price: Double,

    @SerialName("description")
	val description: String,

    @SerialName("type")
	val type: String,

    @SerialName("choices")
	val choices: List<ProductChoice>?,

    @SerialName("status")
	val status: String
) : Parcelable
