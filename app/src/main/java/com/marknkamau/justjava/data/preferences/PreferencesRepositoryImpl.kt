package com.marknkamau.justjava.data.preferences

import android.content.Context
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.User
import com.marknjunge.core.utils.appJsonConfig
import com.marknkamau.justjava.utils.PreferenceUtils

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {
    companion object {
        const val USER_KEY = "user"
        const val SESSION_ID_KEY = "session_id"
    }

    private val prefUtils by lazy {
        PreferenceUtils(
            context.getSharedPreferences("justjava_prefs", Context.MODE_PRIVATE),
            appJsonConfig
        )
    }

    override val isSignedIn: Boolean
        get() = prefUtils.getObject(USER_KEY, User.serializer()) != null

    override var sessionId: String
        get() = prefUtils.get(SESSION_ID_KEY)
        set(value) = prefUtils.set(SESSION_ID_KEY, value)

    override var user: User?
        get() = prefUtils.getObject(USER_KEY, User.serializer())!!
        set(value) = prefUtils.setObject(USER_KEY, value, User.serializer())
}
