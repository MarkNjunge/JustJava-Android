package com.marknkamau.justjavastaff

import android.app.Application
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.StaffDatabaseImpl
import com.marknjunge.core.data.firebase.StaffDatabaseService

import com.marknkamau.justjavastaff.data.local.SettingsRepoImpl
import com.marknkamau.justjavastaff.data.local.SettingsRespository

import timber.log.Timber

class JustJavaStaffApp : Application() {
    lateinit var auth: AuthService
    lateinit var settingsRepository: SettingsRespository
    lateinit var colorUtils: ColorUtils
    lateinit var databaseService: StaffDatabaseService

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "Timber" + super.createStackElementTag(element) + "." + element.methodName
                }
            })
        }

        auth = com.marknjunge.core.auth.AuthServiceImpl()
        settingsRepository = SettingsRepoImpl(this)
        colorUtils = ColorUtils(this)

        databaseService = StaffDatabaseImpl()

    }
}
