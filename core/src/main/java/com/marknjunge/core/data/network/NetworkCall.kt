package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.utils.parseException

@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> call(block: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(block())
    } catch (e: Exception) {
        return parseException(e)
    }
}
