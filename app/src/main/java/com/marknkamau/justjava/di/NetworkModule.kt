package com.marknkamau.justjava.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.data.network.service.FirebaseService
import com.marknjunge.core.data.network.service.GoogleSignInService
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.data.network.AppFirebaseService
import com.marknkamau.justjava.data.network.GoogleSignInServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiService(networkProvider: NetworkProvider) = networkProvider.apiService

    @Provides
    fun provideAuthService(networkProvider: NetworkProvider) = networkProvider.authService

    @Provides
    fun provideCartService(networkProvider: NetworkProvider) = networkProvider.cartService

    @Provides
    fun provideOrdersService(networkProvider: NetworkProvider) = networkProvider.ordersService

    @Provides
    fun providePaymentService(networkProvider: NetworkProvider) = networkProvider.paymentsService

    @Provides
    fun provideUsersService(networkProvider: NetworkProvider) = networkProvider.usersService

    @Provides
    fun provideFirebaseService(): FirebaseService = AppFirebaseService()

    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.googleClientId)
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    fun provideGoogleSignInService(client: GoogleSignInClient): GoogleSignInService = GoogleSignInServiceImpl(client)

    @Provides
    fun provideNetworkProvider(
        @ApplicationContext context: Context,
        preferencesRepository: PreferencesRepository
    ): NetworkProvider {
        return NetworkProvider(context, preferencesRepository)
    }
}
