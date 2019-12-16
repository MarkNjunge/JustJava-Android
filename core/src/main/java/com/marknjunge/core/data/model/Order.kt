package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(

	@SerialName("additionalComments")
	val additionalComments: String,

	@SerialName("datePlaced")
	val datePlaced: Int,

	@SerialName("totalPrice")
	val totalPrice: Int,

	@SerialName("paymentMethod")
	val paymentMethod: String,

	@SerialName("id")
	val id: Int,

	@SerialName("userId")
	val userId: Int,

	@SerialName("items")
	val items: List<OrderItem>,

	@SerialName("paymentStatus")
	val paymentStatus: String,

	@SerialName("status")
	val status: String,

	@SerialName("addressId")
	val addressId: Int
)