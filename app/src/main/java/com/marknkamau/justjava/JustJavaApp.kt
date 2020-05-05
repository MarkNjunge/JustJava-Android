package com.marknkamau.justjava

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknjunge.core.di.repositoriesModule
import com.marknkamau.justjava.di.appModule
import com.marknkamau.justjava.di.dbModule
import com.marknkamau.justjava.di.viewModelModule
import com.marknkamau.justjava.utils.toast
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
open class JustJavaApp : Application() {
    private val preferencesRepository: PreferencesRepository by inject()
    private val usersRepository: UsersRepository by inject()
    private val authRepository: AuthRepository by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${super.createStackElementTag(element)}.${element.methodName}"
                }
            })
        } else {
            Sentry.init(BuildConfig.sentryDsn, AndroidSentryClientFactory(this))
        }

        startKoin {
            androidContext(this@JustJavaApp)
            modules(
                listOf(
                    appModule,
                    repositoriesModule,
                    viewModelModule,
                    dbModule
                )
            )
        }

        Places.initialize(this, getString(R.string.google_api_key))

        if (preferencesRepository.isSignedIn) {
            coroutineScope.launch {
                when (val resource = usersRepository.updateFcmToken()) {
                    is Resource.Success -> usersRepository.getCurrentUser().collect { }
                    is Resource.Failure -> {
                        if (resource.response.message == "Invalid session-id") {
                            Timber.d("Signed out")
                            authRepository.signOutLocally()
                        } else {
                            toast(resource.response.message)
                        }
                    }
                }
            }
        }
    }
}
