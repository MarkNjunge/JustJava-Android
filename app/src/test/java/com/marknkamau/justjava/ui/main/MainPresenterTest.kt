package com.marknkamau.justjava.ui.main

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock private lateinit var view: MainView

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        presenter = MainPresenter(view)
    }

    @Test
    fun should_displayCatalogItems() {
        presenter.getCatalogItems()

        Mockito.verify(view).displayCatalog(Mockito.anyList())
    }

}