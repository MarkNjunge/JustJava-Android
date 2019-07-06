package com.marknjunge.core.data.local

import com.marknjunge.core.model.CoffeeDrink

object DrinksProvider {
    val drinksList: List<CoffeeDrink> = listOf(
            CoffeeDrink("0", "Cappuccino", "Foam, milk and espresso.",
                    "A single, perfectly extracted shot of espresso is marbled with freshly steamed " + "milk to create this coffeehouse staple.",
                    "150", "cappuccino.jpg"),
            CoffeeDrink("1", "Americano", "Water and espresso.",
                    "Italian espresso gets the American treatment; hot water fills the cup for a rich " + "alternative to drip coffee.",
                    "120", "americano.jpg"),
            CoffeeDrink("2", "Espresso", "Just a shot of espresso.",
                    "Our beans are deep roasted, our shots hand-pulled. Taste the finest, freshly " + "ground espresso shot in town.",
                    "100", "espresso.jpg"),
            CoffeeDrink("3", "Macchiato", "Foam and Espresso.",
                    "What could top a hand-pulled shot of our richest, freshest espresso? A dollop " + "of perfectly steamed foam.",
                    "140", "macchiato.jpg"),
            CoffeeDrink("4", "Mocha", "Milk, chocolate syrup and espresso.",
                    "A marriage made in heaven. Espresso, steamed milk and our finest Dutch Cocoa. " + "Whipped cream officiates.",
                    "150", "mocha.jpg"),
            CoffeeDrink("5", "Latte", "Milk and espresso.",
                    "This coffee house favorite adds silky steamed milk to shots of rich espresso, " + "finished with a layer of foam.",
                    "150", "latte.jpg"),
            CoffeeDrink("6", "Iced Coffee", "Milk, espresso and ice.",
                    "Rich espresso, sweetened condensed milk, and cold milk poured over ice.",
                    "170", "iced-coffee.jpg"),
            CoffeeDrink("7", "Cafe frappe", "Ice cream, espresso and whipped cream.",
                    "Rich espresso and Dutch Cocoa are pulled, then poured over ice cream for true " + "refreshment. Topped with whipped cream.",
                    "180", "cafe-frappe.jpg")
    ).sortedBy { it.drinkName }

    fun calculateTotal(coffeeDrink: CoffeeDrink, quantity: Int, cinnamon: Boolean, chocolate: Boolean, marshmallow: Boolean): Int {
        var base = Integer.parseInt(coffeeDrink.drinkPrice)
        base *= quantity
        if (cinnamon) {
            base += quantity * 50
        }
        if (chocolate) {
            base += quantity * 50
        }
        if (marshmallow) {
            base += quantity * 50
        }
        return base
    }
}
