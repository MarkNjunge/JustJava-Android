package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOrderResponse(
    @SerialName("itemId")
	   val itemId: Int,

    @SerialName("errorString")
	   val errorString: String,

    @SerialName("errorType")
	   val errorType: ErrorType,

    @SerialName("errorModel")
	   val errorModel: ErrorModel,

    @SerialName("index")
	   val index: Int,

    @SerialName("newPrice")
	   val newPrice: Double
)
