package com.marknkamau.justjava.data.local

import com.marknjunge.core.model.UserDetails

interface PreferencesRepository {
    fun saveUserDetails(userDetails: UserDetails)
    fun getUserDetails(): UserDetails
    fun clearUserDetails()
}
