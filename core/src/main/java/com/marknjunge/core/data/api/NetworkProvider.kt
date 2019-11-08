package com.marknjunge.core.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.marknjunge.core.BuildConfig
import com.marknjunge.core.payments.PaymentsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class NetworkProvider {
    private val apiUrl = "https://us-central1-justjava-android.cloudfunctions.net/payments/"
    private val apiBaseUrl = BuildConfig.API_BASE_URL

    val paymentsService: PaymentsService
    val apiService: ApiService

    init {
        val retrofit = provideRetrofit()
        paymentsService = retrofit.create(PaymentsService::class.java)
        apiService = provideUpdatedRetrofit().create(ApiService::class.java)
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(provideLoggingCapableHttpClient())
            .build()
    }

    private fun provideUpdatedRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(provideLoggingCapableHttpClient())
            .build()
    }

    private fun provideLoggingCapableHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}