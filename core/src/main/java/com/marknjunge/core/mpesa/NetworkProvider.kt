package com.marknjunge.core.mpesa

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class NetworkProvider {
    private val apiUrl = "https://sandbox.safaricom.co.ke/"

    val mpesaService: MpesaService

    init {
        val retrofit = provideRetrofit(apiUrl)
        mpesaService = retrofit.create<MpesaService>(MpesaService::class.java)
    }

    private fun provideRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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