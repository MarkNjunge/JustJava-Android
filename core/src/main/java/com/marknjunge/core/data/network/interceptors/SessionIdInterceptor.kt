package com.marknjunge.core.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SessionIdInterceptor(private val sessionId: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder().header("session-id", sessionId)
        Timber.d("Added session-id header to request")

        val request = builder.build()
        return chain.proceed(request)
    }
}