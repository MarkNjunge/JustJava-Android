package com.marknkamau.justjavastaff.data.local

import com.marknkamau.justjavastaff.models.StatusSettings

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */
interface SettingsRespository {
    fun getStatusSettings(): StatusSettings
}