package com.marknjunge.core.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
        @SerializedName("message")
        val message: String
)