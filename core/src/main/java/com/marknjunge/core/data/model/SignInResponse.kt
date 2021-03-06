package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("user")
    val user: User,

    @SerialName("session")
    val session: Session
)
