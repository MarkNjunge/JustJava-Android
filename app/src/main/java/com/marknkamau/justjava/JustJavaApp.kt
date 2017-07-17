package com.marknkamau.justjava

import android.app.Application
import com.crashlytics.android.Crashlytics

import com.marknkamau.justjava.dagger.AppComponent
import com.marknkamau.justjava.dagger.DaggerAppComponent
import com.marknkamau.justjava.dagger.FirebaseModule
import com.marknkamau.justjava.dagger.PreferencesRepositoryModule
import io.fabric.sdk.android.Fabric

import io.realm.Realm
import timber.log.Timber

class JustJavaApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        if (!BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            Fabric.with(this, Crashlytics())
        }

        appComponent = DaggerAppComponent
                .builder()
                .preferencesRepositoryModule(PreferencesRepositoryModule(this))
                .firebaseModule(FirebaseModule())
                .build()
    }

}
