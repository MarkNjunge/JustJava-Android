package com.marknkamau.justjava.ui.main

import com.marknjunge.core.model.CoffeeDrink

internal interface MainView {
    fun displayCatalog(drinkList: List<CoffeeDrink>)
}
