package com.marknjunge.core.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import com.marknjunge.core.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

internal class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isInternetAvailable()) {
            throw NoInternetException()
        }

        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.activeNetworkInfo.also {
            return it != null
        }
    }
}
