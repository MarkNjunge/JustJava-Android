package com.marknkamau.justjava.data.local

import com.marknkamau.justjava.data.models.UserDetails

interface PreferencesRepository {
    fun saveUserDetails(userDetails: UserDetails)
    fun getUserDetails(): UserDetails
    fun clearUserDetails()
}
