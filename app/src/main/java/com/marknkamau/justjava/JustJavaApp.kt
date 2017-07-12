package com.marknkamau.justjava

import android.app.Application

import com.marknkamau.justjava.dagger.AppComponent
import com.marknkamau.justjava.dagger.DaggerAppComponent
import com.marknkamau.justjava.dagger.PreferencesRepositoryModule

import io.realm.Realm
import timber.log.Timber

class JustJavaApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
            }
        })

        appComponent = DaggerAppComponent.builder().preferencesRepositoryModule(PreferencesRepositoryModule(this)).build()
    }

}
