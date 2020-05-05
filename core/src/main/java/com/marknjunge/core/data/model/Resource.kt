package com.marknjunge.core.data.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val response: ApiResponse) : Resource<T>()
}
