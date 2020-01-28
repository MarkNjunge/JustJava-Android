package com.marknjunge.core.di

import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.data.repository.*
import org.koin.dsl.module

val repositoriesModule = module {
    val networkProvider = NetworkProvider()

    single<AuthRepository> { ApiAuthRepository(networkProvider.authService, get(), get()) }
    single<ProductsRepository> { ApiProductsRepository(networkProvider.apiService) }
    single<UsersRepository> { ApiUsersRepository(networkProvider.usersService, get(), get()) }
    single<CartRepository> { ApiCartRepository(networkProvider.cartService) }
    single<OrdersRepository> { ApiOrdersRepository(networkProvider.ordersService, get()) }
    single<PaymentsRepository> { ApiPaymentsRepository(networkProvider.paymentsService, get()) }
}