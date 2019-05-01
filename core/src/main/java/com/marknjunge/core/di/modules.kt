package com.marknjunge.core.di

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.auth.AuthServiceImpl
import com.marknjunge.core.data.firebase.ClientDatabaseImpl
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.StaffDatabaseImpl
import com.marknjunge.core.data.firebase.StaffDatabaseService
import com.marknjunge.core.mpesa.MpesaInteractor
import com.marknjunge.core.mpesa.MpesaInteractorImpl
import org.koin.dsl.module

val authModule = module{
    single<AuthService> { AuthServiceImpl() }
}

val databaseModule = module {
    single<ClientDatabaseService> { ClientDatabaseImpl() }
    single<StaffDatabaseService> { StaffDatabaseImpl() }
}

val mpesaModule = module{
    single<MpesaInteractor> { MpesaInteractorImpl() }
}