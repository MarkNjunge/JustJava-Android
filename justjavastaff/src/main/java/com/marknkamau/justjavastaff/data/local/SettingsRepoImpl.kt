package com.marknkamau.justjavastaff.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.models.StatusSettings

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */
class SettingsRepoImpl(private val context: Context) : SettingsRespository {

    lateinit var prefPending: String
    lateinit var prefInProgress: String
    lateinit var prefCompleted: String
    lateinit var prefDelivered: String
    lateinit var prefCancelled: String
    lateinit var preferences: SharedPreferences

    init {
        with(context) {
            prefPending = getString(R.string.PENDING_ORDERS_KEY)
            prefInProgress = getString(R.string.IN_PROGRESS_ORDERS_KEY)
            prefCompleted = getString(R.string.COMPLETED_ORDERS_KEY)
            prefDelivered = getString(R.string.DELIVERED_ORDERS_KEY)
            prefCancelled = getString(R.string.CANCELLED_ORDERS_KEY)

            preferences = PreferenceManager.getDefaultSharedPreferences(this@with)
        }
    }

    override fun getStatusSettings(): StatusSettings {
        return StatusSettings(
                preferences.getBoolean(prefPending, true),
                preferences.getBoolean(prefInProgress, true),
                preferences.getBoolean(prefCompleted, true),
                preferences.getBoolean(prefDelivered, true),
                preferences.getBoolean(prefCancelled, true)
        )
    }

}