package com.marknjunge.core.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val appJsonConfig: Json
    get() = Json { ignoreUnknownKeys = true }
