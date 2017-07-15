package com.marknkamau.justjava.ui.main

import com.marknkamau.justjava.data.MockPreferencesRepository
import com.marknkamau.justjava.network.MockAuthenticationServiceImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainActivityPresenterTest {
    private lateinit var mockView: MainView
        private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        mockView = Mockito.mock(MainView::class.java)

        presenter = MainPresenter(mockView, MockPreferencesRepository, MockAuthenticationServiceImpl)
    }

    @Test
    fun shouldDisplayCatalogItems() {
        presenter.getCatalogItems()
        Mockito.verify(mockView).displayCatalog(Mockito.anyList())
    }

    @Test
    fun shouldSetSignInStatus(){
        presenter.getSignInStatus()
        Mockito.verify(mockView).setSignInStatus(Mockito.anyBoolean())
    }

    @Test
    fun shouldSetSignInStatusOnSignOut(){
        presenter.signOut()
        Mockito.verify(mockView).setSignInStatus(Mockito.anyBoolean())
    }

}