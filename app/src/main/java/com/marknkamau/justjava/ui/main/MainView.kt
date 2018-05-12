package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.models.CoffeeDrink

internal interface MainView {
    fun displayCatalog(drinkList: MutableList<CoffeeDrink>)
}
