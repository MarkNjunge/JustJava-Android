package com.marknkamau.justjava.activities.main;

import com.marknkamau.justjava.models.CoffeeDrink;

import java.util.List;

interface MainActivityView {
    void displayCatalog(List<CoffeeDrink> drinkList);
}
