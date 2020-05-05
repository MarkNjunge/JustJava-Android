package com.marknjunge.core.data.network.service

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.marknjunge.core.BuildConfig
import com.marknjunge.core.data.network.interceptors.ConvertNoContentInterceptor
import com.marknjunge.core.data.network.interceptors.NetworkConnectionInterceptor
import com.marknjunge.core.utils.appConfig
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal class NetworkProvider(private val context: Context) {
    private val apiBaseUrl = BuildConfig.API_BASE_URL
    private val mediaType = "application/json".toMediaType()

    val apiService: ApiService
    val authService: AuthService
    val usersService: UsersService
    val cartService: CartService
    val ordersService: OrdersService
    val paymentsService: PaymentsService

    init {
        val retrofit = provideRetrofit()
        apiService = retrofit.create(ApiService::class.java)
        authService = retrofit.create(AuthService::class.java)
        usersService = retrofit.create(UsersService::class.java)
        cartService = retrofit.create(CartService::class.java)
        ordersService = retrofit.create(OrdersService::class.java)
        paymentsService = retrofit.create(PaymentsService::class.java)
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(JsonConfiguration.appConfig.asConverterFactory(mediaType))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(provideOkHttpClient())
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(NetworkConnectionInterceptor(context))
        builder.addNetworkInterceptor(ConvertNoContentInterceptor())
        builder.addNetworkInterceptor(loggingInterceptor)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.connectTimeout(30, TimeUnit.SECONDS)

        return builder
            .build()
    }
}
