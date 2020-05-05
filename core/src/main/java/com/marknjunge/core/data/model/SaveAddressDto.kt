package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SaveAddressDto(
    @SerialName("streetAddress")
    val streetAddress: String,

    @SerialName("deliveryInstructions")
    val deliveryInstructions: String?,

    @SerialName("latLng")
    val latLng: String
)
