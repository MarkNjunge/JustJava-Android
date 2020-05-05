package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOrderItemDto(
    @SerialName("index")
	   val index: Int,
    @SerialName("productId")
	   val productId: Long,
    @SerialName("productBasePrice")
	   val productBasePrice: Double,
    @SerialName("options")
	   val options: List<VerifyOrderItemOptionDto>

)
