package com.marknjunge.core.data.local

import com.marknjunge.core.data.model.User

interface PreferencesRepository {
    val isSignedIn: Boolean
    var sessionId: String
    var user: User?
}
