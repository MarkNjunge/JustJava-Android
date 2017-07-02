package com.marknkamau.justjava.data

import android.content.SharedPreferences
import com.marknkamau.justjava.models.UserDefaults

import com.marknkamau.justjava.utils.Constants

class PreferencesRepositoryImpl(private val sharedPreferences: SharedPreferences) : PreferencesRepository {

    override fun saveDefaults(userDefaults: UserDefaults) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.DEF_NAME, userDefaults.name)
        editor.putString(Constants.DEF_PHONE, userDefaults.phone)
        editor.putString(Constants.DEF_ADDRESS, userDefaults.defaultAddress)

        editor.apply()
    }

    override fun getDefaults(): UserDefaults {
        return UserDefaults(
                sharedPreferences.getString(Constants.DEF_NAME, ""),
                sharedPreferences.getString(Constants.DEF_NAME, ""),
                sharedPreferences.getString(Constants.DEF_ADDRESS, "")
        )
    }

    override fun clearDefaults() {
        val editor = sharedPreferences.edit()
        editor.remove(Constants.DEF_NAME)
        editor.remove(Constants.DEF_PHONE)
        editor.remove(Constants.DEF_ADDRESS)
        editor.apply()
    }
}
