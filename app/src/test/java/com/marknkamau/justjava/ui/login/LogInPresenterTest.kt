package com.marknkamau.justjava.ui.login

import com.marknkamau.justjava.MockDatabase
import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.anyAuthActionListener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LogInPresenterTest {
    @Mock
    private lateinit var view: LogInView
    @Mock
    private lateinit var auth: AuthenticationService
    @Mock
    private lateinit var preferences: PreferencesRepository

    private lateinit var presenter: LogInPresenter

    @Before
    fun setup() {
        presenter = LogInPresenter(view, preferences, auth, MockDatabase)
    }

    @Test
    fun shouldCloseActivityIfSignedIn() {
        Mockito.`when`(auth.isSignedIn()).thenReturn(true)

        presenter.checkSignInStatus()
        Mockito.verify(view).closeActivity()
    }

    @Test
    fun shouldSignIn() {
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        presenter.signIn("", "")
        Mockito.verify(view).finishSignUp()
    }

    @Test
    fun shouldDisplayError_whenFailedToGetDefaults() {
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        presenter.signIn("", "")
        Mockito.verify(view).displayMessage("")
    }

    @Test
    fun shouldDisplayError_whenFailedToSignIn() {
        Mockito.doAnswer { invocation ->
            val listener = invocation.arguments[2] as AuthenticationService.AuthActionListener
            listener.actionFailed("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        presenter.signIn("", "")
        Mockito.verify(view).displayMessage("")
    }

    @Test
    fun shouldSendPasswordResetEmail() {
        Mockito.doAnswer { invocation ->
            val listener = invocation.arguments[1] as AuthenticationService.AuthActionListener
            listener.actionSuccessful("")
        }.`when`(auth).sendPasswordResetEmail(Mockito.anyString(), anyAuthActionListener())

        presenter.resetUserPassword("")
        Mockito.verify(view).displayMessage("")
    }

    @Test
    fun shouldDisplayError_whenFailedToSendPasswordResetEmail() {
        Mockito.doAnswer { invocation ->
            val listener = invocation.arguments[1] as AuthenticationService.AuthActionListener
            listener.actionFailed("")
        }.`when`(auth).sendPasswordResetEmail(Mockito.anyString(), anyAuthActionListener())

        presenter.resetUserPassword("")
        Mockito.verify(view).displayMessage("")
    }
    
}