package com.marknjunge.core.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val JsonConfiguration.Companion.appConfig: Json
    get() = Json(Stable.copy(ignoreUnknownKeys = true))
