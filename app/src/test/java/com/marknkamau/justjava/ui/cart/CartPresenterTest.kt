package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartRepository
import com.marknkamau.justjava.data.MockPreferencesRepository
import com.marknkamau.justjava.models.CartItem
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CartPresenterTest {
    lateinit var mockView: MockView
    lateinit var mockCart: CartRepository

    @Before
    fun setup() {
        mockView = MockView()

        mockCart = Mockito.mock(CartRepository::class.java)

        val cartPresenter: CartPresenter = CartPresenter(mockView, MockPreferencesRepository(), mockCart)
        cartPresenter.loadItems()
    }

//    @Test
//    fun shouldDisplayCart() {
//        Mockito.`when`(mockCart.getAllCartItems()).thenReturn(Arrays.asList(CartItem(), CartItem(), CartItem()))
//        Mockito.verify(mockCart).getAllCartItems()
//        Assert.assertEquals(true, mockView.cartDisplayed)
//    }

    @Test
    fun shouldDisplayEmptyCart() {
//        Mockito.`when`(mockCart.getAllCartItems()).thenReturn(Collections.emptyList())
        Assert.assertEquals(true, mockView.emptyCartDisplayed)
    }

    @Test
    fun shouldDisplayCartTotal() {
//        Mockito.`when`(mockCart.getTotalPrice()).thenReturn(0)
        Assert.assertEquals(true, mockView.cartTotalDisplayed)
    }

    class MockView : CartView {

        var cartDisplayed = false
        var emptyCartDisplayed = false
        var cartTotalDisplayed = false

        override fun displayCart(cartItems: MutableList<CartItem>) {
            cartDisplayed = true
        }

        override fun displayEmptyCart() {
            emptyCartDisplayed = true
        }

        override fun displayCartTotal(totalCost: Int) {
            cartTotalDisplayed = true
        }

    }
}
