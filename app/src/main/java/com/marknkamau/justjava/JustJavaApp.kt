package com.marknkamau.justjava

import android.app.Application
import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics

import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.data.PreferencesRepositoryImpl
import com.marknkamau.justjava.network.AuthenticationService
import com.marknkamau.justjava.network.AuthenticationServiceImpl
import com.marknkamau.justjava.network.DatabaseService
import com.marknkamau.justjava.network.DatabaseServiceImpl
import io.fabric.sdk.android.Fabric

import io.realm.Realm
import timber.log.Timber

class JustJavaApp : Application() {
    lateinit var preferencesRepo: PreferencesRepository
    lateinit var authService: AuthenticationService
    lateinit var databaseService: DatabaseService

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            Fabric.with(this, Crashlytics())
        }

        preferencesRepo = PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(this))
        authService = AuthenticationServiceImpl
        databaseService = DatabaseServiceImpl
    }

}
