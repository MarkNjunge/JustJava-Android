package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.DrinksProvider
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.utils.FirebaseAuthUtils

internal class MainPresenter(private val activityView: MainView, private val preferences: PreferencesRepository) {

    fun getCatalogItems() {
        activityView.displayCatalog(DrinksProvider.drinksList)
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        preferences.clearDefaults()
    }
}
