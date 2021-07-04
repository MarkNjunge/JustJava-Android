package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFcmTokenDto(
    @SerialName("fcmToken")
    val fcmToken: String
)
