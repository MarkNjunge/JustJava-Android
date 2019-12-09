package com.marknkamau.justjavastaff

import android.app.Application
import com.marknjunge.core.data.network.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.data.firebase.PaymentService
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.di.databaseModule

import com.marknkamau.justjavastaff.data.local.SettingsRespository
import com.marknkamau.justjavastaff.di.appModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

import timber.log.Timber

class JustJavaStaffApp : Application() {
    val auth: AuthService by inject()
    val settingsRepository: SettingsRespository by inject()
    val colorUtils: ColorUtils by inject()
    val paymentService: PaymentService by inject()
    val orderService:OrderService by inject()
    val userService: UserService by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "Timber" + super.createStackElementTag(element) + "." + element.methodName
                }
            })
        }

        startKoin {
            androidContext(this@JustJavaStaffApp)
            modules(listOf(appModule, databaseModule))
        }
    }
}
