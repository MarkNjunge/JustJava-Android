package com.marknkamau.justjavastaff.ui.login

import com.marknkamau.justjavastaff.authentication.AuthenticationService
import com.marknkamau.justjavastaff.models.Employee
import com.nhaarman.mockito_kotlin.any

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
class LoginPresenterTest {
    @Mock
    private val auth: AuthenticationService? = null

    @Mock
    private val view: LoginView? = null

    private var presenter: LoginPresenter? = null

    @Before
    fun setUp() {
        presenter = LoginPresenter(auth!!, view!!)
    }

    @Test
    fun should_signIn() {
        Mockito.doAnswer({
            val authListener = it.arguments[2] as AuthenticationService.AuthListener
            authListener.onSuccess(Employee("", "", ""))
        }
        ).`when`<AuthenticationService>(auth).signIn(Mockito.anyString(), Mockito.anyString(), any())

        presenter!!.signIn("mark@justjava.com", "")

        Mockito.verify<LoginView>(view).onSignedIn()
    }

    @Test
    fun should_showError_onFailedSignedIn() {
        Mockito.doAnswer {
            val authListener = it.arguments[2] as AuthenticationService.AuthListener
            authListener.onError("")
        }.`when`<AuthenticationService>(auth).signIn(Mockito.anyString(), Mockito.anyString(), any())

        presenter!!.signIn("mark@justjava.com", "")

        Mockito.verify<LoginView>(view).displayMessage(Mockito.anyString())
    }

    @Test
    fun should_showError_onInvalidEmail() {
        presenter!!.signIn("mark@mail.com", "")

        Mockito.verify<LoginView>(view).displayMessage(Mockito.anyString())
    }
}