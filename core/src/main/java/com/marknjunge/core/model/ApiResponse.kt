package com.marknjunge.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
        @SerialName("message")
        val message: String
)