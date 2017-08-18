package com.marknkamau.justjava.ui.main

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainActivityPresenterTest {
    private lateinit var mockView: MainView
        private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        mockView = Mockito.mock(MainView::class.java)

        presenter = MainPresenter(mockView)
    }

    @Test
    fun shouldDisplayCatalogItems() {
        presenter.getCatalogItems()
        Mockito.verify(mockView).displayCatalog(Mockito.anyList())
    }
}