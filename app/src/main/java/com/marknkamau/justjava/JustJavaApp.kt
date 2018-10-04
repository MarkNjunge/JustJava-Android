package com.marknkamau.justjava

import android.app.Application
import android.arch.persistence.room.Room
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import com.marknkamau.justjava.data.network.authentication.AuthenticationService
import com.marknkamau.justjava.data.network.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknkamau.justjava.data.network.db.DatabaseServiceImpl
import com.marknkamau.justjava.data.network.NetworkProvider
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.mpesa.Mpesa
import timber.log.Timber
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.Crashlytics



class JustJavaApp : Application() {
    lateinit var preferencesRepo: PreferencesRepository
    lateinit var authService: AuthenticationService
    lateinit var databaseService: DatabaseService
    lateinit var cartDatabase: CartDatabase
    lateinit var notificationHelper: NotificationHelper
    lateinit var mpesa: Mpesa
    lateinit var broadcastManager: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        }else{
            val fabric = Fabric.Builder(this)
                    .kits(Crashlytics())
                    .build()
            Fabric.with(fabric)
        }

        preferencesRepo = PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(this))
        authService = AuthenticationServiceImpl
        databaseService = DatabaseServiceImpl()
        mpesa = Mpesa(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, NetworkProvider().mpesaService)

        cartDatabase = Room.databaseBuilder(this, CartDatabase::class.java, "cart-db").build()

        notificationHelper = NotificationHelper(this)

        broadcastManager = LocalBroadcastManager.getInstance(this)
    }

}
