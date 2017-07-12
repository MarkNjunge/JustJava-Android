package com.marknkamau.justjava.dagger

import com.marknkamau.justjava.network.AuthenticationService
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule{
    @Provides
    fun provideAuthenticationService():AuthenticationService{
        return AuthenticationServiceImpl
    }
}

