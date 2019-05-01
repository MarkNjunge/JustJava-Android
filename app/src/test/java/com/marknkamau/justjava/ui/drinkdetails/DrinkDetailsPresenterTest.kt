package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class DrinkDetailsPresenterTest {

    @MockK
    private lateinit var view: DrinkDetailsView
    @MockK
    private lateinit var cartDao: CartDao

    private lateinit var presenter: DrinkDetailsPresenter
    private val cartItem = CartItem(1, "itemName", 1, false, false, false, 10)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = DrinkDetailsPresenter(view, cartDao, Dispatchers.Unconfined)
    }

    @Test
    fun should_addToCart_success() {
        coEvery { cartDao.addItem(any()) } returns Unit

        presenter.addToCart(cartItem)

        verify { view.finishActivity() }
    }

    @Test
    fun should_addToCart_fail() {
        val message = "Error"
        coEvery { cartDao.addItem(any()) } throws Exception(message)

        presenter.addToCart(cartItem)

        verify { view.displayMessage(message) }
    }

}