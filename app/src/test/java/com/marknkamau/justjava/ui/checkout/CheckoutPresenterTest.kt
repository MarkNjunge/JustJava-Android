package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknjunge.core.model.UserDetails
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import io.mockk.MockKAnnotations
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

class CheckoutPresenterTest {

    @MockK
    private lateinit var view: CheckoutView
    @MockK
    private lateinit var auth: AuthService
    @MockK
    private lateinit var preferences: PreferencesRepository
    @MockK
    private lateinit var orderService: OrderService
    @MockK
    private lateinit var cartDao: CartDao

    private lateinit var presenter: CheckoutPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = CheckoutPresenter(view, auth, preferences, orderService, cartDao, Dispatchers.Unconfined)
    }

    @Test
    fun getSignInStatus_signedIn() {
        val userDetails = UserDetails("", "", "", "", "")
        every { auth.isSignedIn() } returns true
        every { preferences.getUserDetails() } returns userDetails

        presenter.getSignInStatus()

        verify { view.setDisplayToLoggedIn(userDetails) }
    }

    @Test
    fun getSignInStatus_notSignedIn() {
        every { auth.isSignedIn() } returns false

        presenter.getSignInStatus()

        verify { view.setDisplayToLoggedOut() }
    }

}