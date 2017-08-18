package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.MockCartRepoImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CartPresenterTest {
    private lateinit var mockView: CartView
    private lateinit var cartPresenter: CartPresenter

    @Before
    fun setup() {
        mockView = Mockito.mock(CartView::class.java)

        cartPresenter = CartPresenter(mockView, MockCartRepoImpl)
    }

    @Test
    fun shouldDisplayCartItems() {
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayCart(Mockito.anyList())
    }

    @Test
    fun shouldDisplayCartTotal() {
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayCartTotal(Mockito.anyInt())
    }

    @Test
    fun shouldClearCart(){
        cartPresenter.clearCart()
        Mockito.verify(mockView).displayEmptyCart()
    }
}
