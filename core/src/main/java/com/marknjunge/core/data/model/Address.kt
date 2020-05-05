package com.marknjunge.core.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Address(
    @SerialName("id")
    val id: Long,

    @SerialName("streetAddress")
    val streetAddress: String,

    @SerialName("deliveryInstructions")
    val deliveryInstructions: String?,

    @SerialName("latLng")
    val latLng: String
) : Parcelable
