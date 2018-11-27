package com.marknkamau.justjava.ui.login

import com.marknjunge.core.auth.AuthService
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknjunge.core.model.UserDetails
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
class LogInPresenterTest {

    @Mock private lateinit var view: LogInView
    @Mock private lateinit var preferences: PreferencesRepository
    @Mock private lateinit var auth: AuthService
    @Mock private lateinit var database: DatabaseService

    private lateinit var presenter: LogInPresenter

    @Before
    fun setup() {
        presenter = LogInPresenter(view, preferences, auth, database)
    }

    @Test
    fun should_closeActivity_when_signedIn() {
        Mockito.`when`(auth.isSignedIn()).thenReturn(true)

        presenter.checkSignInStatus()

        Mockito.verify(view).closeActivity()
    }

    @Test
    fun should_finishSignIn_when_signInAndUserDefaults_success() {
        // Succeed in signing in
        Mockito.doAnswer { invocation ->
            val authActionListener = invocation.arguments[2] as AuthService.AuthActionListener
            authActionListener.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), any())

        // Succeed in getting user defaults
        Mockito.doAnswer { invocation ->
            val userDetailsListener = invocation.arguments[1] as DatabaseService.UserDetailsListener
            userDetailsListener.onSuccess(UserDetails("", "", "", "", ""))
        }.`when`(database).getUserDefaults(Mockito.anyString(), any())

        presenter.signIn("", "")

        Mockito.verify(view).finishSignIn()
    }

    @Test
    fun should_displayMessage_when_signIn_failed() {
        // Fail in signing in
        Mockito.doAnswer { invocation ->
            val authActionListener = invocation.arguments[2] as AuthService.AuthActionListener
            authActionListener.actionFailed("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), any())

        presenter.signIn("", "")

        Mockito.verify(view).displayMessage(Mockito.any())
    }

    @Test
    fun should_displayMessage_when_getUserDefaults_failed() {
        // Succeed in signing in
        Mockito.doAnswer { invocation ->
            val authActionListener = invocation.arguments[2] as AuthService.AuthActionListener
            authActionListener.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), any())

        // Fail in getting user defaults
        Mockito.doAnswer { invocation ->
            val userDetailsListener = invocation.arguments[1] as DatabaseService.UserDetailsListener
            userDetailsListener.onError("")
        }.`when`(database).getUserDefaults(Mockito.anyString(), any())

        presenter.signIn("", "")

        Mockito.verify(view).displayMessage(Mockito.any())
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_success() {
        Mockito.doAnswer { invocation ->
            val authActionListener = invocation.arguments[1] as AuthService.AuthActionListener
            authActionListener.actionSuccessful("")
        }.`when`(auth).sendPasswordResetEmail(Mockito.anyString(), any())

        presenter.resetUserPassword("")

        Mockito.verify(view).displayMessage(Mockito.any())
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_failed() {
        Mockito.doAnswer { invocation ->
            val authActionListener = invocation.arguments[1] as AuthService.AuthActionListener
            authActionListener.actionFailed("")
        }.`when`(auth).sendPasswordResetEmail(Mockito.anyString(), any())

        presenter.resetUserPassword("")

        Mockito.verify(view).displayMessage(Mockito.any())
    }
}