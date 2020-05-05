package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(

    @SerialName("additionalComments")
	   val additionalComments: String?,

    @SerialName("datePlaced")
	   val datePlaced: Long,

    @SerialName("totalPrice")
	   val totalPrice: Double,

    @SerialName("paymentMethod")
	   val paymentMethod: PaymentMethod,

    @SerialName("id")
	   val id: String,

    @SerialName("userId")
	   val userId: Int,

    @SerialName("items")
	   val items: List<OrderItem>,

    @SerialName("paymentStatus")
	   val paymentStatus: PaymentStatus,

    @SerialName("status")
	   val status: OrderStatus,

    @SerialName("addressId")
	   val addressId: Long? = null
)
