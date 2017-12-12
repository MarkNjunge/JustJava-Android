package com.marknkamau.justjava.data.local

import com.marknkamau.justjava.models.UserDefaults

interface PreferencesRepository {
    fun saveDefaults(userDefaults: UserDefaults)
    fun getDefaults(): UserDefaults
    fun clearDefaults()
}
