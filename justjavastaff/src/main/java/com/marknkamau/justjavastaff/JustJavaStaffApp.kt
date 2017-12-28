package com.marknkamau.justjavastaff

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.marknkamau.justjavastaff.authentication.AuthServiceImpl
import com.marknkamau.justjavastaff.authentication.AuthenticationService
import com.marknkamau.justjavastaff.data.network.OrdersRepository
import com.marknkamau.justjavastaff.data.network.OrdersRepositoryImpl

import timber.log.Timber

class JustJavaStaffApp : Application() {
    lateinit var preferences: SharedPreferences
    lateinit var auth: AuthenticationService
    lateinit var ordersRepository: OrdersRepository

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "Timber" + super.createStackElementTag(element) + "." + element.methodName
                }
            })
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        auth = AuthServiceImpl()
        ordersRepository = OrdersRepositoryImpl()
    }
}
