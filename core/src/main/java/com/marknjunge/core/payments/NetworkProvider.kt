package com.marknjunge.core.payments

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class NetworkProvider {
    private val apiUrl = "https://us-central1-justjava-android.cloudfunctions.net/payments/"

    val paymentsService: PaymentsService

    init {
        val retrofit = provideRetrofit()
        paymentsService = retrofit.create(PaymentsService::class.java)
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(apiUrl)
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