package com.marknkamau.justjava.ui.main;

import com.marknkamau.justjava.models.CoffeeDrink;

import java.util.List;

interface MainActivityView {
    void displayCatalog(List<CoffeeDrink> drinkList);
}
