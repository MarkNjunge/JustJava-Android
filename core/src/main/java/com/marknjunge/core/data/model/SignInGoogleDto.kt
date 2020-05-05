package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignInGoogleDto(
    @SerialName("idToken")
    val idToken: String
)
