package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderItemDto(
    @SerialName("quantity")
	   val quantity: Int,

    @SerialName("productId")
	   val productId: Long,

    @SerialName("options")
	   val options: List<PlaceOrderItemOptionsDto>
)
