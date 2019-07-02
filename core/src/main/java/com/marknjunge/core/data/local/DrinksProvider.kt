package com.marknjunge.core.data.local

import com.marknjunge.core.model.CoffeeDrink

object DrinksProvider {
    val drinksList: MutableList<CoffeeDrink> = mutableListOf()

    init {
        addItem(CoffeeDrink("0", "Cappuccino", "Foam, milk and espresso.",
                "A single, perfectly extracted shot of espresso is marbled with freshly steamed " + "milk to create this coffeehouse staple.",
                "150", "cappuccino.jpg"))
        addItem(CoffeeDrink("1", "Americano", "Water and espresso.",
                "Italian espresso gets the American treatment; hot water fills the cup for a rich " + "alternative to drip coffee.",
                "120", "americano.jpg"))
        addItem(CoffeeDrink("2", "Espresso", "Just a shot of espresso.",
                "Our beans are deep roasted, our shots hand-pulled. Taste the finest, freshly " + "ground espresso shot in town.",
                "100", "espresso.jpg"))
        addItem(CoffeeDrink("3", "Macchiato", "Foam and Espresso.",
                "What could top a hand-pulled shot of our richest, freshest espresso? A dollop " + "of perfectly steamed foam.",
                "140", "macchiato.jpg"))
        addItem(CoffeeDrink("4", "Mocha", "Milk, chocolate syrup and espresso.",
                "A marriage made in heaven. Espresso, steamed milk and our finest Dutch Cocoa. " + "Whipped cream officiates.",
                "150", "mocha.jpg"))
        addItem(CoffeeDrink("5", "Latte", "Milk and espresso.",
                "This coffee house favorite adds silky steamed milk to shots of rich espresso, " + "finished with a layer of foam.",
                "150", "latte.jpg"))
        addItem(CoffeeDrink("6", "Iced Coffee", "Milk, espresso and ice.",
                "Rich espresso, sweetened condensed milk, and cold milk poured over ice.",
                "170", "iced-coffee.jpg"))
        addItem(CoffeeDrink("7", "Cafe frappe", "Ice cream, espresso and whipped cream.",
                "Rich espresso and Dutch Cocoa are pulled, then poured over ice cream for true " + "refreshment. Topped with whipped cream.",
                "200", "cafe-frappe.jpg"))
    }

    private fun addItem(drink: CoffeeDrink) {
        drinksList.add(drink)
    }
}
