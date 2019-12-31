package com.marknkamau.justjava

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.marknjunge.core.di.repositoriesModule
import com.marknkamau.justjava.di.appModule
import com.marknkamau.justjava.di.dbModule
import com.marknkamau.justjava.di.viewModelModule
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
open class JustJavaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            Sentry.init(BuildConfig.sentryDsn, AndroidSentryClientFactory(this))
        }

        startKoin {
            androidContext(this@JustJavaApp)
            modules(
                listOf(
                    appModule,
                    repositoriesModule,
                    viewModelModule,
                    dbModule
                )
            )
        }

        Places.initialize(this, getString(R.string.google_api_key))
    }

}
