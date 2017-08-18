package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.MockCartRepoImpl
import com.marknkamau.justjava.data.MockPreferencesRepository
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.network.MockAuthenticationServiceImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DrinkDetailsPresenterTest {
    lateinit var mockView: DrinkDetailsView
    internal lateinit var presenter: DrinkDetailsPresenter

    @Before
    fun setup() {
        mockView = Mockito.mock(DrinkDetailsView::class.java)
        presenter = DrinkDetailsPresenter(mockView, MockCartRepoImpl)
    }

    @Test
    fun shouldAddItemToCart() {
        presenter.addToCart(CartItem())
        Mockito.verify(mockView).finishActivity()
    }

}