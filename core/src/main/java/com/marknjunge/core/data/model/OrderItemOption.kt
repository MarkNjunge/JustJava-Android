package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemOption(
    @SerialName("choiceId")
	   val choiceId: Int,

    @SerialName("choiceName")
	   val choiceName: String,

    @SerialName("optionId")
	   val optionId: Int,

    @SerialName("optionPrice")
	   val optionPrice: Double,

    @SerialName("id")
	   val id: Int,

    @SerialName("optionName")
	   val optionName: String
)
