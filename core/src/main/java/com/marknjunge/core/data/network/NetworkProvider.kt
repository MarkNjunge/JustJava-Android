package com.marknjunge.core.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.marknjunge.core.BuildConfig
import com.marknjunge.core.data.network.interceptors.ConvertNoContentInterceptor
import com.marknjunge.core.data.network.interceptors.SessionIdInterceptor
import com.marknjunge.core.payments.PaymentsService
import com.marknjunge.core.utils.appConfig
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

internal class NetworkProvider {
    private val apiUrl = "https://us-central1-justjava-android.cloudfunctions.net/payments/"
    private val apiBaseUrl = BuildConfig.API_BASE_URL
    private val mediaType = "application/json".toMediaType()

    val paymentsService: PaymentsService
    val apiService: ApiService
    val authService: AuthService
    val usersService: UsersService
    val cartService: CartService
    val ordersService: OrdersService

    init {
        val legacyRetrofit = provideLegacyRetrofit()
        paymentsService = legacyRetrofit.create(PaymentsService::class.java)

        val retrofit = provideRetrofit()
        apiService = retrofit.create(ApiService::class.java)
        authService = retrofit.create(AuthService::class.java)
        usersService = retrofit.create(UsersService::class.java)
        cartService = retrofit.create(CartService::class.java)
        ordersService = retrofit.create(OrdersService::class.java)
    }

    private fun provideLegacyRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(JsonConfiguration.appConfig.asConverterFactory(mediaType))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(provideOkHttpClient())
            .build()
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
        builder.addNetworkInterceptor(ConvertNoContentInterceptor())
        builder.addNetworkInterceptor(loggingInterceptor)

        return builder
            .build()
    }
}