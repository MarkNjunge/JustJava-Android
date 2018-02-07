package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.models.CartItem
import io.reactivex.Single
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


/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */


@RunWith(MockitoJUnitRunner::class)
class CartPresenterTest {

    @Mock private lateinit var view: CartView
    @Mock private lateinit var cartDao: CartDao

    private lateinit var presenter: CartPresenter
    private val cartItem = CartItem(1, "itemName", 1, false, false, false, 10)
    private val cartItems = mutableListOf(cartItem)

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = CartPresenter(view, cartDao)
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }


    @Test
    fun test_loadItems_success() {
        Mockito.`when`(cartDao.getAll()).thenReturn(Single.just(cartItems))

        presenter.loadItems()

        Mockito.verify(view).displayCart(cartItems)
    }

    @Test
    fun test_loadItems_success_emptyList() {
        Mockito.`when`(cartDao.getAll()).thenReturn(Single.just(mutableListOf()))

        presenter.loadItems()

        Mockito.verify(view).displayEmptyCart()
    }

    @Test
    fun test_loadItems_fail() {
        val message = "Error"
        Mockito.doAnswer {
            Single.error<Exception>(Exception(message))
        }.`when`(cartDao).getAll()

        presenter.loadItems()

        Mockito.verify(view).displayMessage(message)
    }

    @Test
    fun test_clearCart_success() {
        presenter.clearCart()

        Mockito.verify(view).displayEmptyCart()
    }

    @Test
    fun test_clearCart_fail() {
        val message = "Error"
        Mockito.doAnswer {
            Single.error<Exception>(Exception(message))
        }.`when`(cartDao).deleteAll()

        presenter.clearCart()

        Mockito.verify(view).displayEmptyCart()
    }

    @Test
    fun test_deleteItem_success() {
        Mockito.`when`(cartDao.getAll()).thenReturn(Single.just(cartItems))

        presenter.deleteItem(cartItem)

        Mockito.verify(view).displayMessage(Mockito.anyString())
        Mockito.verify(view).displayCart(cartItems)
    }

    @Test
    fun test_deleteItem_fail() {
        val message = "Error"
        Mockito.doAnswer {
            Single.error<Exception>(Exception(message))
        }.`when`(cartDao).getAll()

        presenter.deleteItem(cartItem)

        Mockito.verify(view).displayMessage(message)
    }

    @Test
    fun test_updateItem_success() {
        Mockito.`when`(cartDao.getAll()).thenReturn(Single.just(cartItems))

        presenter.updateItem(cartItem)

        Mockito.verify(view).displayMessage(Mockito.anyString())
        Mockito.verify(view).displayCart(cartItems)
    }
    @Test
    fun test_updateItem_fail() {
        val message = "Error"
        Mockito.doAnswer {
            Single.error<Exception>(Exception(message))
        }.`when`(cartDao).getAll()

        presenter.updateItem(cartItem)

        Mockito.verify(view).displayMessage(message)
    }

}