package com.marknkamau.justjava

import android.app.Application
import androidx.room.Room
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.auth.AuthServiceImpl
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.utils.NotificationHelper
import timber.log.Timber
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.Crashlytics
import com.marknjunge.core.data.firebase.ClientDatabaseImpl
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknjunge.core.mpesa.MpesaInteractor
import com.marknjunge.core.mpesa.MpesaInteractorImpl
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
            modules(appModule)
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
