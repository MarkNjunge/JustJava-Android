package com.marknkamau.justjava

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.di.databaseModule
import com.marknjunge.core.di.paymentsModule
import com.marknjunge.core.di.repositoriesModule
import com.marknkamau.justjava.di.appModule
import com.marknkamau.justjava.di.dbModule
import com.marknkamau.justjava.di.presentersModule
import com.marknkamau.justjava.di.viewModelModule
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import timber.log.Timber

@Suppress("unused")
open class JustJavaApp : Application() {
//    private val authService: AuthService by inject()
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
                    presentersModule,
                    databaseModule,
                    paymentsModule,
                    repositoriesModule,
                    viewModelModule,
                    dbModule
                )
            )
        }


//        if (authService.isSignedIn()) {
//            val user = authService.getCurrentUser()
//
//            scope.launch {
//                try {
//                    userService.updateUserFcmToken(user.userId)
//                    Timber.i("FCM token saved")
//                } catch (e: Exception) {
//                    Timber.e(e)
//                }
//            }
//
//        }
    }

}
