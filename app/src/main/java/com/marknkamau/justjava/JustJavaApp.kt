package com.marknkamau.justjava

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.utils.ReleaseTree
import com.marknkamau.justjava.utils.toast
import dagger.hilt.android.HiltAndroidApp
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Suppress("unused")
@HiltAndroidApp
open class JustJavaApp : Application() {
    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    @Inject
    lateinit var usersRepository: UsersRepository
    @Inject
    lateinit var authRepository: AuthRepository

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber ${element.methodName} (${element.fileName}:${element.lineNumber})"
                }
            })
        } else {
            Timber.plant(ReleaseTree())
            SentryAndroid.init(this) { options ->
                options.dsn = BuildConfig.sentryDsn
            }
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
