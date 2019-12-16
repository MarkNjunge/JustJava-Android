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
	val totalPrice: Int,

	@SerialName("productBasePrice")
	val productBasePrice: Int,

	@SerialName("id")
	val id: Int,

	@SerialName("productName")
	val productName: String,

	@SerialName("option")
	val option: List<OrderItemOption>
)