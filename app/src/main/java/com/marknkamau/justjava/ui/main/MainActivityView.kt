package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.models.CoffeeDrink

internal interface MainActivityView {
    fun displayCatalog(drinkList: MutableList<CoffeeDrink>)
}
