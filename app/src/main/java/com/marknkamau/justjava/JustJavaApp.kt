package com.marknkamau.justjava

import android.arch.persistence.room.Room
import android.preference.PreferenceManager
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.marknkamau.justjava.data.network.authentication.AuthenticationService
import com.marknkamau.justjava.data.network.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknkamau.justjava.data.network.db.DatabaseServiceImpl
import com.marknkamau.justjava.data.network.MpesaService
import com.marknkamau.justjava.data.network.NetworkProvider
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.mpesa.Mpesa
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class JustJavaApp : MultiDexApplication() {
    lateinit var preferencesRepo: PreferencesRepository
    lateinit var authService: AuthenticationService
    lateinit var databaseService: DatabaseService
    lateinit var cartDatabase: CartDatabase
    lateinit var notificationHelper: NotificationHelper
    lateinit var mpesa: Mpesa

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            Fabric.with(this, Crashlytics())
        }

        preferencesRepo = PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(this))
        authService = AuthenticationServiceImpl
        databaseService = DatabaseServiceImpl()
        mpesa = Mpesa(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, NetworkProvider().mpesaService)

        cartDatabase = Room.databaseBuilder(this, CartDatabase::class.java, "cart-db").build()

        notificationHelper = NotificationHelper(this)
    }

}
