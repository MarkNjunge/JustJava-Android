package com.marknkamau.justjava.data.models

import com.google.gson.annotations.SerializedName

data class OAuthAccess(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("expires_in")
        val expiresIn: String
)