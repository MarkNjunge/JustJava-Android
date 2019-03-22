package com.marknkamau.justjava

import android.app.Application
import androidx.room.Room
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.auth.AuthServiceImpl
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.utils.NotificationHelper
import timber.log.Timber
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.Crashlytics
import com.marknjunge.core.data.firebase.ClientDatabaseImpl
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknjunge.core.mpesa.MpesaInteractor
import com.marknjunge.core.mpesa.MpesaInteractorImpl
import com.squareup.leakcanary.LeakCanary

class JustJavaApp : Application() {
    lateinit var preferencesRepo: PreferencesRepository
    lateinit var authService: AuthService
    lateinit var databaseService: ClientDatabaseService
    lateinit var cartDatabase: CartDatabase
    lateinit var notificationHelper: NotificationHelper
    lateinit var mpesaInteractor: MpesaInteractor
    lateinit var broadcastManager: androidx.localbroadcastmanager.content.LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

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
        authService = AuthServiceImpl()
        databaseService = ClientDatabaseImpl()
        mpesaInteractor = MpesaInteractorImpl()

        cartDatabase = Room.databaseBuilder(this, CartDatabase::class.java, "cart-db").build()

        notificationHelper = NotificationHelper(this)

        broadcastManager = androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this)

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
