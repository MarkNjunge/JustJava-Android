package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.MockDatabase
import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDetails
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

@RunWith(MockitoJUnitRunner::class)
class CheckoutPresenterTest {
    @Mock
    private lateinit var view: CheckoutView
    @Mock
    private lateinit var cart: CartDao
    @Mock
    private lateinit var auth: AuthenticationService
    @Mock
    private lateinit var preferences: PreferencesRepository

    private lateinit var presenter: CheckoutPresenter
    private val cartItems = mutableListOf(CartItem(0, "", 0, true, true, true, 0))

    @Before
    fun setUp() {
        presenter = CheckoutPresenter(view, auth, preferences, MockDatabase, cart)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun shouldDisplayLoggedIn() {
        val userDefaults = UserDetails("", "", "")
        Mockito.`when`(preferences.getUserDetails()).thenReturn(userDefaults)
        Mockito.`when`(auth.isSignedIn()).thenReturn(true)

        presenter.getSignInStatus()
        Mockito.verify(view).setDisplayToLoggedIn(userDefaults)
    }

    @Test
    fun shouldDisplayLoggedOut() {
        Mockito.`when`(auth.isSignedIn()).thenReturn(false)

        presenter.getSignInStatus()
        Mockito.verify(view).setDisplayToLoggedOut()
    }

    @Test
    fun shouldPlaceOrder() {
        val order = Order("", "", 0, 0, "", "")

        Mockito.`when`(cart.getAll()).thenReturn(Single.just(cartItems))

        presenter.placeOrder(order)

        Mockito.verify(view).finishActivity()
    }

    @Test
    fun shouldDisplayError_whenPlacingOrderFails() {
        Mockito.`when`(cart.getAll()).thenReturn(Single.just(cartItems))
        presenter.placeOrder(Order("", "", 0, 0, "", ""))

        Mockito.verify(view).displayMessage("")
    }
}