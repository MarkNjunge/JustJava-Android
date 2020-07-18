package com.marknjunge.core.data.network.interceptors

import com.marknjunge.core.data.local.PreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response

class SessionIdInterceptor(private val preferencesRepository: PreferencesRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (preferencesRepository.isSignedIn) {
            chain.request().newBuilder().addHeader("session-id", preferencesRepository.sessionId).build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
