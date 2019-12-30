package com.marknjunge.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.marknjunge.core.data.firebase.*
import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.data.repository.*
import com.marknjunge.core.payments.LegacyPaymentsRepository
import com.marknjunge.core.payments.LegacyPaymentsRepositoryImpl
import org.koin.dsl.module

val databaseModule = module {
    val firestore = FirebaseFirestore.getInstance()

    single<UserService> { UserServiceImpl(firestore) }
    single<OrderService> { OrderServiceImpl(firestore) }
    single<PaymentService> { PaymentServiceImpl(firestore) }
}

val paymentsModule = module {
    single<LegacyPaymentsRepository> { LegacyPaymentsRepositoryImpl() }
}

val repositoriesModule = module {
    val networkProvider = NetworkProvider()

    single<AuthRepository> { ApiAuthRepository(networkProvider.authService, get()) }
    single<ProductsRepository> { ApiProductsRepository(networkProvider.apiService) }
    single<UsersRepository> { ApiUsersRepository(networkProvider.usersService, get()) }
    single<CartRepository> { ApiCartRepository(networkProvider.cartService) }
    single<OrdersRepository> { ApiOrdersRepository(networkProvider.ordersService, get()) }
    single<PaymentsRepository> { ApiPaymentsRepository(networkProvider.paymentsService, get()) }
}