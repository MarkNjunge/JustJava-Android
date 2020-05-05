package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDto(
    @SerialName("token")
    val token: String,
    @SerialName("newPassword")
    val newPassword: String
)
