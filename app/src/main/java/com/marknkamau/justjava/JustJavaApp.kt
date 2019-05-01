package com.marknkamau.justjava

import android.app.Application
import com.marknjunge.core.auth.AuthService
import timber.log.Timber
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.Crashlytics
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknjunge.core.di.authModule
import com.marknjunge.core.di.databaseModule
import com.marknjunge.core.di.mpesaModule
import com.marknkamau.justjava.di.appModule
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JustJavaApp : Application() {
    private val authService: AuthService by inject()
    private val databaseService: ClientDatabaseService by inject()

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

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
            modules(appModule, databaseModule, mpesaModule, authModule)
        }

        if (authService.isSignedIn()) {
            val user = authService.getCurrentUser()

            databaseService.updateUserFcmToken(user.userId, object : WriteListener {
                override fun onError(reason: String) {
                    Timber.e(reason)
                }

                override fun onSuccess() {
                    Timber.i("FCM token saved")
                }

            })
        }
    }

}
