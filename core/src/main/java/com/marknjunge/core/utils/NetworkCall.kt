package com.marknjunge.core.utils

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.model.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException
import retrofit2.HttpException
import timber.log.Timber

internal suspend fun <T> call(block: suspend () -> Resource<T>): Resource<T> {
    return try {
        block()
    } catch (e: Exception) {
        return parseException(e)
    }
}
