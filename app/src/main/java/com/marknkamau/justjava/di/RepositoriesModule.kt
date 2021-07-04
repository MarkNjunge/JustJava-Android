package com.marknkamau.justjava.di

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.network.service.*
import com.marknjunge.core.data.repository.*
import com.marknkamau.justjava.data.db.CartDao
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.db.DbRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {
    @Provides
    fun providesDbRepository(cardDao: CartDao): DbRepository = DbRepositoryImpl(cardDao)

    @Provides
    fun provideProductsRepository(apiService: ApiService): ProductsRepository = ApiProductsRepository(apiService)

    @Provides
    fun provideAuthRepository(
        authService: AuthService,
        preferencesRepository: PreferencesRepository,
        googleSignInService: GoogleSignInService
    ): AuthRepository = ApiAuthRepository(authService, preferencesRepository, googleSignInService)

    @Provides
    fun provideCartRepository(cartService: CartService): CartRepository = ApiCartRepository(cartService)

    @Provides
    fun provideOrdersRepository(ordersService: OrdersService): OrdersRepository = ApiOrdersRepository(ordersService)

    @Provides
    fun providePaymentsRepository(
        paymentsService: PaymentsService
    ): PaymentsRepository = ApiPaymentsRepository(paymentsService)

    @Provides
    fun provideUsersRepository(
        usersService: UsersService,
        preferencesRepository: PreferencesRepository,
        googleSignInService: GoogleSignInService,
        firebaseService: FirebaseService
    ): UsersRepository = ApiUsersRepository(usersService, preferencesRepository, googleSignInService, firebaseService)
}
