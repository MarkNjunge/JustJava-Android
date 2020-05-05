package com.marknjunge.core.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    @SerialName("id")
    val id: Int,

    @SerialName("firstName")
    val firstName: String,

    @SerialName("lastName")
    val lastName: String,

    @SerialName("createdAt")
    val createdAt: Long,

    @SerialName("mobileNumber")
    val mobileNumber: String?,

    @SerialName("email")
    val email: String,

    @SerialName("fcmToken")
    val fcmToken: String?,

    // TODO Use enum
    @SerialName("signInMethod")
    val signInMethod: String,

    @SerialName("addresses")
    val address: List<Address>
) : Parcelable
