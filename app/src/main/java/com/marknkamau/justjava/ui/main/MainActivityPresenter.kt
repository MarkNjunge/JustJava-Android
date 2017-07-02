package com.marknkamau.justjava.ui.main

import android.content.SharedPreferences

import com.marknkamau.justjava.models.DataProvider
import com.marknkamau.justjava.utils.Constants
import com.marknkamau.justjava.utils.FirebaseAuthUtils

internal class MainActivityPresenter(private val activityView: MainActivityView, private val sharedPreferences: SharedPreferences) {

    fun getCatalogItems() {
        activityView.displayCatalog(DataProvider.drinksList)
    }

    fun logUserOut() {
        FirebaseAuthUtils.logOut()
        val editor = sharedPreferences.edit()
        editor.remove(Constants.DEF_NAME)
        editor.remove(Constants.DEF_PHONE)
        editor.remove(Constants.DEF_ADDRESS)
        editor.apply()
    }
}
