package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.MockPreferencesRepository
import com.marknkamau.justjava.network.MockAuthenticationServiceImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainActivityPresenterTest {
    private lateinit var mockView: MainView

    @Before
    fun setup() {
        mockView = Mockito.mock(MainView::class.java)

        val presenter = MainPresenter(mockView, MockPreferencesRepository, MockAuthenticationServiceImpl)

        presenter.getSignInStatus()
        presenter.getCatalogItems()
        presenter.signOut()
    }

    @Test
    fun shouldDisplayCatalogItems() {
        Mockito.verify(mockView).displayCatalog(Mockito.anyList())
    }

    @Test
    fun shouldSetSignInStatus(){
        Mockito.verify(mockView, Mockito.atLeast(2)).setSignInStatus(Mockito.anyBoolean())
    }

}