package com.marknkamau.justjava.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.local.DrinksProvider
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

        onView(withId(R.id.rvCatalog)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(drinksList.size - 1))
        onView(withText(lastItemName)).check(matches(isDisplayed()))

    }

    @Test
    fun testCanOpenDetailsActivity() {
        val drinksList = DrinksProvider.drinksList
        onView(withText(drinksList[0].drinkName)).perform(ViewActions.click())
        onView(withText(drinksList[0].drinkName)).check(matches(isDisplayed()))
    }
}
