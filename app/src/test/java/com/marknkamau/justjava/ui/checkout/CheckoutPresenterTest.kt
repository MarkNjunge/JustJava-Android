package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.data.network.authentication.AuthenticationService
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknkamau.justjava.data.network.MpesaService
import com.marknkamau.justjava.data.models.UserDetails
import com.marknkamau.justjava.utils.mpesa.Mpesa
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
class CheckoutPresenterTest {

    @Mock
    private lateinit var view: CheckoutView
    @Mock
    private lateinit var auth: AuthenticationService
    @Mock
    private lateinit var preferences: PreferencesRepository
    @Mock
    private lateinit var database: DatabaseService
    @Mock
    private lateinit var cartDao: CartDao

    private lateinit var presenter: CheckoutPresenter

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = CheckoutPresenter(view, auth, preferences, database, cartDao)
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun getSignInStatus_signedIn() {
        val userDetails = UserDetails("", "", "", "", "")
        Mockito.`when`(auth.isSignedIn()).thenReturn(true)
        Mockito.`when`(preferences.getUserDetails()).thenReturn(userDetails)

        presenter.getSignInStatus()

        Mockito.verify(view).setDisplayToLoggedIn(userDetails)
    }

    @Test
    fun getSignInStatus_notSignedIn() {
        Mockito.`when`(auth.isSignedIn()).thenReturn(false)

        presenter.getSignInStatus()

        Mockito.verify(view).setDisplayToLoggedOut()
    }

}