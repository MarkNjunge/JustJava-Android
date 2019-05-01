package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class CartPresenterTest {

    @MockK
    private lateinit var view: CartView
    @MockK
    private lateinit var cartDao: CartDao

    private lateinit var presenter: CartPresenter
    private val cartItem = CartItem(1, "itemName", 1, false, false, false, 10)
    private val cartItems = mutableListOf(cartItem)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        presenter = CartPresenter(view, cartDao, Dispatchers.Unconfined)
    }

    @Test
    fun test_loadItems_success() {
        coEvery { cartDao.getAll() } returns cartItems

        presenter.loadItems()

        verify { view.displayCart(cartItems) }
    }

    @Test
    fun test_loadItems_success_emptyList() {
        coEvery { cartDao.getAll() } returns mutableListOf()

        presenter.loadItems()

        verify { view.displayEmptyCart() }
    }

    @Test
    fun test_loadItems_fail() {
        val message = "Error"
        coEvery { cartDao.getAll() } throws Exception(message)

        presenter.loadItems()

        verify { view.displayMessage(message) }
    }

    @Test
    fun test_clearCart_success() {
        presenter.clearCart()

        verify { view.displayEmptyCart() }
    }

    @Test
    fun test_clearCart_fail() {
        val message = "Error"
        coEvery { cartDao.getAll() } throws Exception(message)

        presenter.clearCart()

        verify { view.displayEmptyCart() }
    }

    @Test
    fun test_deleteItem_success() {
        coEvery { cartDao.getAll() } returns cartItems
        coEvery { cartDao.deleteItem(any()) } returns Unit

        presenter.deleteItem(cartItem)

        verify { view.displayMessage(any()) }
        verify { view.displayCart(cartItems) }
    }

    @Test
    fun test_deleteItem_fail() = runBlocking {
        val message = "Error"
        coEvery { cartDao.getAll() } throws Exception(message)

        presenter.deleteItem(cartItem)

        verify { view.displayMessage(message) }
    }

    @Test
    fun test_updateItem_success() = runBlocking {
        coEvery { cartDao.getAll() } returns cartItems

        presenter.updateItem(cartItem)

        verify { view.displayMessage(any()) }
        verify { view.displayCart(cartItems) }
    }

    @Test
    fun test_updateItem_fail() = runBlocking {
        val message = "Error"
        coEvery { cartDao.getAll() } throws Exception(message)

        presenter.updateItem(cartItem)

        verify { view.displayMessage(message) }
    }

}