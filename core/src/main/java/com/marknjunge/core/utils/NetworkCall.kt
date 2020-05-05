package com.marknjunge.core.utils

import com.marknjunge.core.data.model.Resource

@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> call(block: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(block())
    } catch (e: Exception) {
        return parseException(e)
    }
}
