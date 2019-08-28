package com.marknkamau.justjava.testUtils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers

fun onViewWithId(id: Int): ViewInteraction = Espresso.onView(ViewMatchers.withId(id))

fun onViewWithText(text: String): ViewInteraction = Espresso.onView(ViewMatchers.withText(text))

fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())

fun ViewInteraction.clickRecyclerViewItem(position: Int): ViewInteraction = perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, ViewActions.click())
)

fun ViewInteraction.scrollTo(): ViewInteraction = perform(ViewActions.scrollTo())

fun ViewInteraction.isDisplayed(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.isDisplayed()))