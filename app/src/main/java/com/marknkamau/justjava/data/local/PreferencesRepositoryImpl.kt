package com.marknkamau.justjava.data.local

import android.content.SharedPreferences
import com.marknkamau.justjava.models.UserDetails

class PreferencesRepositoryImpl(private val sharedPreferences: SharedPreferences) : PreferencesRepository {
    private val id = "user_id"
    private val email = "user_email"
    private val name = "user_name"
    private val phone = "user_phone"
    private val address = "user_address"

    override fun saveUserDetails(userDetails: UserDetails) {
        val editor = sharedPreferences.edit()
        editor.putString(id, userDetails.id)
        editor.putString(email, userDetails.email)
        editor.putString(name, userDetails.name)
        editor.putString(phone, userDetails.phone)
        editor.putString(address, userDetails.address)

        editor.apply()
    }

    override fun getUserDetails(): UserDetails {
        return UserDetails(
                sharedPreferences.getString(id, ""),
                sharedPreferences.getString(email, ""),
                sharedPreferences.getString(name, ""),
                sharedPreferences.getString(phone, ""),
                sharedPreferences.getString(address, "")
        )
    }

    override fun clearUserDetails() {
        val editor = sharedPreferences.edit()
        editor.remove(id)
        editor.remove(email)
        editor.remove(name)
        editor.remove(phone)
        editor.remove(address)
        editor.apply()
    }
}
