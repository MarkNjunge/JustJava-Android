package com.marknkamau.justjava.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.utils.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) }
    single { Room.databaseBuilder(androidContext(), CartDatabase::class.java, "cart-db").build() }
    single { get<CartDatabase>().cartDao() }
    single { NotificationHelper(androidContext()) }
}