package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CartPresenterTest {
    @Mock
    private lateinit var mockView: CartView

    @Mock
    private lateinit var cart: CartDao

    private lateinit var cartPresenter: CartPresenter
    private val cartItems = mutableListOf(CartItemRoom(0, "", 0, true, true, true, 0))

    @Before
    fun setup() {
        cartPresenter = CartPresenter(mockView, cart)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown(){
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun shouldDisplayCartItems() {
        Mockito.`when`(cart.getAll()).thenReturn(Flowable.just(cartItems))
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayCart(cartItems)
    }

    @Test
    fun shouldDisplayEmptyCart() {
        Mockito.`when`(cart.getAll()).thenReturn(Flowable.just(Collections.emptyList()))
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayEmptyCart()
    }

    @Test
    fun shouldDisplayCartTotal() {
        Mockito.`when`(cart.getAll()).thenReturn(Flowable.just(cartItems))
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayCartTotal(Mockito.anyInt())
    }

    @Test
    fun shouldHandleError_whenGettingItems() {
        Mockito.`when`(cart.getAll()).thenReturn(Flowable.error(Throwable("Whoosh!")))
        cartPresenter.loadItems()
        Mockito.verify(mockView).displayMessage(Mockito.anyString())
    }

    @Test
    fun shouldClearCart() {
        cartPresenter.clearCart()
        Mockito.verify(mockView).displayEmptyCart()
    }
}
