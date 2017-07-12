package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.DrinksProvider
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.network.AuthenticationService

internal class MainPresenter(private val activityView: MainView, private val preferences: PreferencesRepository, private val auth: AuthenticationService) {

    fun getCatalogItems() {
        activityView.displayCatalog(DrinksProvider.drinksList)
    }

    fun getSignInStatus(){
        activityView.setSignInStatus(auth.isSignedIn())
    }

    fun signOut() {
        auth.logOut()
        preferences.clearDefaults()
        activityView.setSignInStatus(auth.isSignedIn())
    }
}
