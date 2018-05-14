package com.marknkamau.justjavastaff

import android.app.Application

import com.marknkamau.justjavastaff.authentication.AuthServiceImpl
import com.marknkamau.justjavastaff.authentication.AuthenticationService
import com.marknkamau.justjavastaff.data.local.SettingsRepoImpl
import com.marknkamau.justjavastaff.data.local.SettingsRespository
import com.marknkamau.justjavastaff.data.network.DataRepository
import com.marknkamau.justjavastaff.data.network.DataRepositoryImpl

import timber.log.Timber

class JustJavaStaffApp : Application() {
    lateinit var auth: AuthenticationService
    lateinit var dataRepository: DataRepository
    lateinit var settingsRepository: SettingsRespository
    lateinit var colorUtils: ColorUtils

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
        dataRepository = DataRepositoryImpl()
        settingsRepository = SettingsRepoImpl(this)
        colorUtils = ColorUtils(this)
    }
}
