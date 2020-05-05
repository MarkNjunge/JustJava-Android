package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderItemOptionsDto(
    @SerialName("choiceId")
	   val choiceId: Long,

    @SerialName("optionId")
	   val optionId: Long
)
