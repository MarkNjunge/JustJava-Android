package com.marknkamau.justjava

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.di.databaseModule
import com.marknjunge.core.di.paymentsModule
import com.marknjunge.core.di.repositoriesModule
import com.marknkamau.justjava.di.appModule
import com.marknkamau.justjava.di.dbModule
import com.marknkamau.justjava.di.viewModelModule
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
open class JustJavaApp : Application() {
    private val userService: UserService by inject()

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .build()
            Fabric.with(fabric)
        }

        startKoin {
            androidContext(this@JustJavaApp)
            modules(
                listOf(
                    appModule,
                    databaseModule,
                    paymentsModule,
                    repositoriesModule,
                    viewModelModule,
                    dbModule
                )
            )
        }

    }

}
