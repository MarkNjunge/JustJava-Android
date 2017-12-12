package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.local.DrinksProvider

internal class MainPresenter(private val activityView: MainView) {

    fun getCatalogItems() {
        activityView.displayCatalog(DrinksProvider.drinksList)
    }
}
