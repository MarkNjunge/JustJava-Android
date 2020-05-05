package com.marknjunge.core.di

import com.marknjunge.core.data.network.service.NetworkProvider
import com.marknjunge.core.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoriesModule = module {
    single { NetworkProvider(androidContext()) }

    single<AuthRepository> { ApiAuthRepository(get<NetworkProvider>().authService, get(), get()) }
    single<ProductsRepository> { ApiProductsRepository(get<NetworkProvider>().apiService) }
    single<UsersRepository> { ApiUsersRepository(get<NetworkProvider>().usersService, get(), get(), get()) }
    single<CartRepository> { ApiCartRepository(get<NetworkProvider>().cartService, get()) }
    single<OrdersRepository> { ApiOrdersRepository(get<NetworkProvider>().ordersService, get()) }
    single<PaymentsRepository> { ApiPaymentsRepository(get<NetworkProvider>().paymentsService, get()) }
}
