package com.marknkamau.justjava.testUtils

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.repository.*
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.di.RepositoriesModule
import com.marknkamau.justjava.utils.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoriesModule::class]
)
object TestRepositoriesModule {
    val mockPreferencesRepository = mockk<PreferencesRepository>()
    val mockNotificationHelper = mockk<NotificationHelper>()

    val mockAuthRepository = mockk<AuthRepository>()
    val mockProductsRepository = mockk<ProductsRepository>()
    val mockUsersRepository = mockk<UsersRepository>()
    val mockCartRepository = mockk<CartRepository>()
    val mockOrdersRepository = mockk<OrdersRepository>()
    val mockPaymentsRepository = mockk<PaymentsRepository>()

    val mockDbRepository = mockk<DbRepository>()

    @Provides
    fun providesDbRepository(): DbRepository = mockDbRepository

    @Provides
    fun provideProductsRepository(): ProductsRepository = mockProductsRepository

    @Provides
    fun provideAuthRepository(): AuthRepository = mockAuthRepository

    @Provides
    fun provideCartRepository(): CartRepository = mockCartRepository

    @Provides
    fun provideOrdersRepository(): OrdersRepository = mockOrdersRepository

    @Provides
    fun providePaymentsRepository(): PaymentsRepository = mockPaymentsRepository

    @Provides
    fun provideUsersRepository(): UsersRepository = mockUsersRepository
}