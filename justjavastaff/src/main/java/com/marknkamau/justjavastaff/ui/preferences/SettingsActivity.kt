package com.marknkamau.justjavastaff.ui.preferences

import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.appcompat.app.AppCompatActivity

import com.marknkamau.justjavastaff.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesFragment = MyPreferencesFragment()
        fragmentManager
                .beginTransaction()
                .add(android.R.id.content, preferencesFragment)
                .commit()

    }

    class MyPreferencesFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.preferences)
        }
    }
}
