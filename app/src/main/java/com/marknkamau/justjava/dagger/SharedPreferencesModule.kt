package com.marknkamau.justjava.dagger

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule(private val application: Application) {

    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }
}
