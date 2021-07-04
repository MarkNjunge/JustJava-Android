package com.marknjunge.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    @SerialName("firstName")
	   val firstName: String,

    @SerialName("lastName")
	   val lastName: String,

    @SerialName("mobileNumber")
	   val mobileNumber: String,

    @SerialName("email")
	   val email: String
)
