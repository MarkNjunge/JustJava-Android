package com.marknjunge.core.di

import com.google.firebase.firestore.FirebaseFirestore
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.auth.AuthServiceImpl
import com.marknjunge.core.data.firebase.*
import com.marknjunge.core.payments.PaymentsRepository
import com.marknjunge.core.payments.PaymentsRepositoryImpl
import org.koin.dsl.module

val authModule = module {
    single<AuthService> { AuthServiceImpl() }
}

val databaseModule = module {
    val firestore = FirebaseFirestore.getInstance()

    single<UserService> { UserServiceImpl(firestore) }
    single<OrderService> { OrderServiceImpl(firestore) }
    single<PaymentService> { PaymentServiceImpl(firestore) }
}

val paymentsModule = module {
    single<PaymentsRepository> { PaymentsRepositoryImpl() }
}