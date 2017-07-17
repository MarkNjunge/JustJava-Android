package com.marknkamau.justjava.dagger

import com.marknkamau.justjava.network.AuthenticationService
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import com.marknkamau.justjava.network.DatabaseService
import com.marknkamau.justjava.network.DatabaseServiceImpl
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {
    @Provides
    fun provideAuthenticationService(): AuthenticationService {
        return AuthenticationServiceImpl
    }

    @Provides
    fun provideDatabaseService(): DatabaseService {
        return DatabaseServiceImpl
    }
}
