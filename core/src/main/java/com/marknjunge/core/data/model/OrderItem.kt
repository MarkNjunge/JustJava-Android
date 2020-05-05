package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(

    @SerialName("quantity")
	   val quantity: Int,

    @SerialName("productId")
	   val productId: Int,

    @SerialName("totalPrice")
	   val totalPrice: Double,

    @SerialName("productBasePrice")
	   val productBasePrice: Double,

    @SerialName("productName")
	   val productName: String,

    @SerialName("options")
	   val options: List<OrderItemOption>
)
