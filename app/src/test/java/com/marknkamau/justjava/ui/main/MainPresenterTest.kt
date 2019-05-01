package com.marknkamau.justjava.ui.main

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MainPresenterTest {

    @MockK
    private lateinit var view: MainView

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = MainPresenter(view)
    }

    @Test
    fun should_displayCatalogItems() {
        presenter.getCatalogItems()

        verify { view.displayCatalog(any()) }
    }

}