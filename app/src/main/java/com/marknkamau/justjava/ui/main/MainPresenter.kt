package com.marknkamau.justjava.ui.main

import com.marknjunge.core.data.local.DrinksProvider
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher

internal class MainPresenter(private val view: MainView,
                             mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    fun getCatalogItems() {
        view.displayCatalog(DrinksProvider.drinksList)
    }
}
