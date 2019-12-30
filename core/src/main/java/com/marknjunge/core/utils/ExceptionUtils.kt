package com.marknjunge.core.utils

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.ApiResponse
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException
import retrofit2.HttpException
import timber.log.Timber

internal fun <T> parseException(e:Exception):Resource<T>{
    Timber.e(e)
    val genericErrorMessage = "Something went wrong. Please try again."

    return when (e) {
        is HttpException -> {
            e.response()?.errorBody()?.string()?.let { errorString ->
                try {
                    val apiResponse = JsonConfiguration.appConfig.parse(ApiResponse.serializer(), errorString)
                    Timber.e("${e.response()?.code()}, $errorString")
                    Resource.Failure<T>(apiResponse.message)
                } catch (e: JsonDecodingException) {
                    Resource.Failure<T>(errorString)
                }
            } ?: Resource.Failure(genericErrorMessage)
        }
        else -> {
            Resource.Failure(e.message ?: genericErrorMessage)
        }
    }
}