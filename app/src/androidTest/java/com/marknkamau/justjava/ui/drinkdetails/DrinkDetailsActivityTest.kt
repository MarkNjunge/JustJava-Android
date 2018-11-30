package com.marknkamau.justjava.ui.drinkdetails

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.marknkamau.justjava.R
import com.marknjunge.core.data.local.DrinksProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class DrinkDetailsActivityTest {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<DrinkDetailsActivity> = ActivityTestRule(DrinkDetailsActivity::class.java, true, false)

    @Test
    fun testDrinkDetailsAreDisplayed() {
        val coffeeDrink = DrinksProvider.drinksList[0]
        val i = Intent()
        i.putExtra(DrinkDetailsActivity.DRINK_KEY, coffeeDrink)

        activityRule.launchActivity(i)

        onView(withId(R.id.tvDrinkName)).check(matches(withText(coffeeDrink.drinkName)))
        onView(withId(R.id.tvDrinkDescription)).check(matches(withText(coffeeDrink.drinkDescription)))
        onView(withId(R.id.tvDrinkContents)).check(matches(withText(coffeeDrink.drinkContents)))
        onView(withId(R.id.tvDrinkPrice)).check(matches(withText("Ksh.${coffeeDrink.drinkPrice}")))
    }
}