package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("lastUseDate")
    val lastUseDate: Long,

    @SerialName("userId")
    val userId: Int
)
