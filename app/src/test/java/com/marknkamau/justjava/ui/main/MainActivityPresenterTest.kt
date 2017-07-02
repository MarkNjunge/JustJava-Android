package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.models.CoffeeDrink
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainActivityPresenterTest {
    lateinit var mockView: MockView

    @Before
    fun setup() {
        mockView = MockView()

        val mockPreferencesRepo = Mockito.mock(PreferencesRepository::class.java)

        val mockPresenter = MainPresenter(mockView, mockPreferencesRepo)

        mockPresenter.getCatalogItems()
    }

    @Test
    fun shouldDisplayCatalogItems() {
        Assert.assertEquals(true, mockView.displayedCatalog)
    }

    class MockView : MainView {
        var displayedCatalog = false

        override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
            displayedCatalog = true
        }
    }

}