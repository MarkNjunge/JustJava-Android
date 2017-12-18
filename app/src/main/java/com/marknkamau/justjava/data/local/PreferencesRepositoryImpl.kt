package com.marknkamau.justjava.data.local

import android.content.SharedPreferences
import com.marknkamau.justjava.models.UserDefaults

class PreferencesRepositoryImpl(private val sharedPreferences: SharedPreferences) : PreferencesRepository {

    private val name = "defaultName"
    private val phone = "defaultPhoneNumber"
    private val address = "defaultDeliveryAddress"

    override fun saveDefaults(userDefaults: UserDefaults) {
        val editor = sharedPreferences.edit()
        editor.putString(name, userDefaults.name)
        editor.putString(phone, userDefaults.phone)
        editor.putString(address, userDefaults.defaultAddress)

        editor.apply()
    }

    override fun getDefaults(): UserDefaults {
        return UserDefaults(
                sharedPreferences.getString(name, ""),
                sharedPreferences.getString(name, ""),
                sharedPreferences.getString(address, "")
        )
    }

    override fun clearDefaults() {
        val editor = sharedPreferences.edit()
        editor.remove(name)
        editor.remove(phone)
        editor.remove(address)
        editor.apply()
    }
}
