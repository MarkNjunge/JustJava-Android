package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.models.CartItem
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
class DrinkDetailsPresenterTest {

    @Mock private lateinit var view: DrinkDetailsView
    @Mock private lateinit var cartDao: CartDao

    private lateinit var presenter: DrinkDetailsPresenter
    private val cartItem = CartItem(1, "itemName", 1, false, false, false, 10)

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = DrinkDetailsPresenter(view, cartDao)
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun should_addToCart_success() {
        presenter.addToCart(cartItem)

        Mockito.verify(view).finishActivity()
    }

    @Test
    fun should_addToCart_fail() {
        val message = "Error"
        Mockito.doAnswer {
            throw Exception(message)
        }.`when`(cartDao).addItem(cartItem)

        presenter.addToCart(cartItem)

        Mockito.verify(view).displayMessage(message)
    }

}