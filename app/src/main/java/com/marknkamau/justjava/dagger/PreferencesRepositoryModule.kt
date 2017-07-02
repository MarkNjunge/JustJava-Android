package com.marknkamau.justjava.dagger

import android.app.Application
import android.preference.PreferenceManager
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.data.PreferencesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class PreferencesRepositoryModule(private val application: Application){

    @Provides
    fun providePreferenceRepository() : PreferencesRepository {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        return PreferencesRepositoryImpl(sharedPreferences)
    }
}
