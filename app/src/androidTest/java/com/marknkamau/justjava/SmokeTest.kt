package com.marknkamau.justjava

import androidx.test.core.app.ActivityScenario
import com.marknkamau.justjava.testUtils.*
import com.marknkamau.justjava.ui.main.MainActivity
import org.junit.Test

class SmokeTest {
    @Test
    fun canPerformAppFunctions() {
        ActivityScenario.launch(MainActivity::class.java)

        val itemPos = 0
        val drink = DrinksProvider.drinksList[itemPos]

        // Go to drink details
        onViewWithId(R.id.rvCatalog).clickRecyclerViewItem(itemPos)

        // Add item to cart
        onViewWithId(R.id.btnAddToCart).scrollTo().click()

        // Go to cart
        onViewWithId(R.id.menu_cart).click()

        // Confirm item is in cart
        onViewWithText(drink.drinkName).isDisplayed()

        // Place order
        onViewWithId(R.id.btnPlaceOrder).scrollTo().click()
    }
}