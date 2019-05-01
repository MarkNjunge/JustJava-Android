package com.marknkamau.justjavastaff

import android.app.Application
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.StaffDatabaseService
import com.marknjunge.core.di.authModule
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
    val databaseService: StaffDatabaseService by inject()

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
            modules(appModule, databaseModule, authModule)
        }
    }
}
