package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOrderItemOptionDto(
    @SerialName("choiceId")
	   val choiceId: Long,
    @SerialName("optionId")
	   val optionId: Long,
    @SerialName("optionPrice")
	   val optionPrice: Double
)
