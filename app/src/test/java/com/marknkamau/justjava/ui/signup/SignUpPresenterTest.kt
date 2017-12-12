package com.marknkamau.justjava.ui.signup

import com.marknkamau.justjava.MockDatabase
import com.marknkamau.justjava.anyAuthActionListener
import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.local.PreferencesRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpPresenterTest {
    @Mock
    private lateinit var view: SignUpView
    @Mock
    private lateinit var auth: AuthenticationService
    @Mock
    private lateinit var preferences: PreferencesRepository

    private lateinit var presenter: SignUpPresenter

    @Before
    fun setUp() {
        presenter = SignUpPresenter(view, preferences, auth, MockDatabase)
    }

    @Test
    fun shouldCreateUserAndSignIn() {
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).createUser(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[1] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).setUserDisplayName(Mockito.anyString(), anyAuthActionListener())

        presenter.createUser("", "", "", "", "")
        Mockito.verify(view).finishActivity()
    }

    @Test
    fun shouldDisplayError_whenCreateUserFails(){
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionFailed("")
        }.`when`(auth).createUser(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        presenter.createUser("", "", "", "", "")
        Mockito.verify(view).displayMessage("")
    }

    @Test
    fun shouldDisplayError_whenSignInFails(){
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).createUser(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionFailed("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        presenter.createUser("", "", "", "", "")
        Mockito.verify(view).displayMessage("")
    }

    @Test
    fun shouldDisplayErrorWhenSettingDisplayNameFails(){
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).createUser(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[2] as AuthenticationService.AuthActionListener
            callback.actionSuccessful("")
        }.`when`(auth).signIn(Mockito.anyString(), Mockito.anyString(), anyAuthActionListener())

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[1] as AuthenticationService.AuthActionListener
            callback.actionFailed("")
        }.`when`(auth).setUserDisplayName(Mockito.anyString(), anyAuthActionListener())

        presenter.createUser("", "", "", "", "")
        Mockito.verify(view).displayMessage("")
    }

}