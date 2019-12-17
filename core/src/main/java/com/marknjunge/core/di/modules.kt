package com.marknjunge.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.marknjunge.core.data.network.NetworkProvider
import com.marknjunge.core.data.firebase.*
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.repository.*
import com.marknjunge.core.data.repository.ApiProductsRepository
import com.marknjunge.core.data.repository.ApiAuthRepository
import com.marknjunge.core.payments.PaymentsRepository
import com.marknjunge.core.payments.PaymentsRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {
    val firestore = FirebaseFirestore.getInstance()

    single<UserService> { UserServiceImpl(firestore) }
    single<OrderService> { OrderServiceImpl(firestore) }
    single<PaymentService> { PaymentServiceImpl(firestore) }
}

val paymentsModule = module {
    single<PaymentsRepository> { PaymentsRepositoryImpl() }
}

val repositoriesModule = module {
    single(named("no-auth")) {
        NetworkProvider()
    }
    single(named("auth")) {
        val preferencesRepository = get<PreferencesRepository>()
        NetworkProvider(preferencesRepository.sessionId)
    }

    single<AuthRepository> { ApiAuthRepository(get<NetworkProvider>(named("no-auth")).authService, get()) }
    single<ProductsRepository> { ApiProductsRepository(get<NetworkProvider>(named("no-auth")).apiService) }
    single<UsersRepository> { ApiUsersRepository(get<NetworkProvider>(named("auth")).usersService, get()) }
    single<CartRepository> { ApiCartRepository(get<NetworkProvider>(named("no-auth")).cartService) }
    single<OrdersRepository>{ApiOrdersRepository(get<NetworkProvider>(named("auth")).ordersService)}
}