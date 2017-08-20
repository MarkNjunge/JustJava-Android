package com.marknkamau.justjava.ui.main

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityPresenterTest {
    @Mock
    private lateinit var view: MainView

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        presenter = MainPresenter(view)
    }

    @Test
    fun shouldDisplayCatalogItems() {
        presenter.getCatalogItems()
        Mockito.verify(view).displayCatalog(Mockito.anyList())
    }
}