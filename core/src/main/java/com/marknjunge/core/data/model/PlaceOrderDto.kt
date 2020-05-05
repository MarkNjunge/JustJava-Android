package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderDto(
    @SerialName("paymentMethod")
	   val paymentMethod: String,

    @SerialName("items")
	   val items: List<PlaceOrderItemDto>,

    @SerialName("addressId")
	   val addressId: Long,

    @SerialName("additionalComments")
	   val additionalComments: String? = null
)
