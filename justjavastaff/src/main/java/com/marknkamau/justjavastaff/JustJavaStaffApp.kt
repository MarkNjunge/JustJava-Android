package com.marknkamau.justjavastaff

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings

import com.marknkamau.justjavastaff.authentication.AuthServiceImpl
import com.marknkamau.justjavastaff.authentication.AuthenticationService
import com.marknkamau.justjavastaff.data.local.SettingsRepoImpl
import com.marknkamau.justjavastaff.data.local.SettingsRespository
import com.marknkamau.justjavastaff.data.network.OrdersRepository
import com.marknkamau.justjavastaff.data.network.OrdersRepositoryImpl

import timber.log.Timber

class JustJavaStaffApp : Application() {
    lateinit var auth: AuthenticationService
    lateinit var ordersRepository: OrdersRepository
    lateinit var settingsRepository: SettingsRespository

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "Timber" + super.createStackElementTag(element) + "." + element.methodName
                }
            })
        }

        auth = AuthServiceImpl()
        ordersRepository = OrdersRepositoryImpl()
        settingsRepository = SettingsRepoImpl(this)
    }
}
