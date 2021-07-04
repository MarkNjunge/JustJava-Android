package com.marknkamau.justjava.di

import android.content.Context
import androidx.room.Room
import com.marknkamau.justjava.data.db.AppDatabase
import com.marknkamau.justjava.data.db.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "justjava-db")
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }

    @Provides
    fun provideCartDao(appDatabase: AppDatabase): CartDao = appDatabase.cartDao()
}
