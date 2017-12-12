package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItem
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

@RunWith(MockitoJUnitRunner::class)
class DrinkDetailsPresenterTest {
    @Mock
    private lateinit var view: DrinkDetailsView

    @Mock
    private lateinit var cart: CartDao

    internal lateinit var presenter: DrinkDetailsPresenter

    @Before
    fun setup() {
        presenter = DrinkDetailsPresenter(view, cart)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun shouldAddItemToCart() {
        presenter.addToCart(CartItem(0, "", 0, true, true, true, 0))
        Mockito.verify(view).finishActivity()
    }

}