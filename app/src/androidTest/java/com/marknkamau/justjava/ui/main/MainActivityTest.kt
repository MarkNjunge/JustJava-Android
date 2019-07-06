package com.marknkamau.justjava.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.marknjunge.core.data.local.DrinksProvider
import com.marknkamau.justjava.R
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
class MainActivityTest {
    @Rule
    @JvmField
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRecyclerViewVisibleAndHasData() {
        onView(withId(R.id.rvCatalog)).check(matches(isDisplayed()))

        val drinksList = DrinksProvider.drinksList
        val firstItemName = drinksList[0].drinkName
        val lastItemName = drinksList[drinksList.size - 1].drinkName

        onView(withText(firstItemName)).check(matches(isDisplayed()))

        onView(withId(R.id.rvCatalog)).perform(RecyclerViewActions.scrollToPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(drinksList.size - 1))
        onView(withText(lastItemName)).check(matches(isDisplayed()))

    }

    @Test
    fun testCanOpenDetailsActivity() {
        val drinksList = DrinksProvider.drinksList
        onView(withText(drinksList[0].drinkName)).perform(ViewActions.click())
        onView(withText(drinksList[0].drinkName)).check(matches(isDisplayed()))
    }
}
