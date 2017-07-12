package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.models.CoffeeDrink

internal interface MainView {
    fun displayCatalog(drinkList: MutableList<CoffeeDrink>)
    fun setSignInStatus(status: Boolean)
}
