package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOrderDto(
    @SerialName("items")
	   val items: List<VerifyOrderItemDto>
)
