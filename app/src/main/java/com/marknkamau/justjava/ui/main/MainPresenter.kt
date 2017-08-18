package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.DrinksProvider
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.authentication.AuthenticationService

internal class MainPresenter(private val activityView: MainView) {

    fun getCatalogItems() {
        activityView.displayCatalog(DrinksProvider.drinksList)
    }
}
