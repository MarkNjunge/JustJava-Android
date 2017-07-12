package com.marknkamau.justjava.dagger

import com.marknkamau.justjava.network.DatabaseService
import com.marknkamau.justjava.network.DatabaseServiceImpl
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule{
    @Provides
    fun provideDatabaseService(): DatabaseService {
        return DatabaseServiceImpl
    }
}