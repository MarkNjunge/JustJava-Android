package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderItemOptionsDto(
	@SerialName("choiceId")
	val choiceId: Int,

	@SerialName("optionId")
	val optionId: Int
)