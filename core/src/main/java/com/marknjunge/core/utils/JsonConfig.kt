package com.marknjunge.core.utils

import kotlinx.serialization.json.Json

val appJsonConfig: Json
    get() = Json { ignoreUnknownKeys = true }
