package com.marknkamau.justjava.ui.login

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknjunge.core.data.firebase.WriteListener
import com.marknjunge.core.model.AuthUser
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.data.local.PreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LogInPresenterTest {

    @MockK
    private lateinit var view: LogInView
    @MockK
    private lateinit var preferences: PreferencesRepository
    @MockK
    private lateinit var auth: AuthService
    @MockK
    private lateinit var database: ClientDatabaseService

    private lateinit var presenter: LogInPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = LogInPresenter(view, preferences, auth, database)
    }

    @Test
    fun should_closeActivity_when_signedIn() {
        every { auth.isSignedIn() } returns true

        presenter.checkSignInStatus()

        verify { view.closeActivity() }
    }

    @Test
    fun should_finishSignIn_when_signInAndUserDefaults_success() {
        // Succeed in signing in
        every {
            auth.signIn(any(), any(), any())
        } answers {
            val listener = thirdArg<AuthService.AuthActionListener>()
            listener.actionSuccessful("")
        }

        // Succeed in getting user defaults
        every {
            database.getUserDefaults(any(), any())
        } answers {
            val listener = secondArg<ClientDatabaseService.UserDetailsListener>()
            listener.onSuccess(UserDetails("", "", "", "", ""))
        }

        every { auth.getCurrentUser() } returns AuthUser("", "", "")

        // Succeed updating FCM token
        every {
            database.updateUserFcmToken(any(), any())
        } answers {
            val listener = secondArg<WriteListener>()
            listener.onSuccess()
        }

        presenter.signIn("", "")

        verify { view.finishSignIn() }
    }

    @Test
    fun should_displayMessage_when_signIn_failed() {
        // Fail in signing in
        every {
            auth.signIn(any(), any(), any())
        } answers {
            val listener = thirdArg<AuthService.AuthActionListener>()
            listener.actionFailed("")
        }

        presenter.signIn("", "")

        verify { view.displayMessage(any()) }
    }

    @Test
    fun should_displayMessage_when_getUserDefaults_failed() {
        // Succeed in signing in
        every {
            auth.signIn(any(), any(), any())
        } answers {
            val listener = thirdArg<AuthService.AuthActionListener>()
            listener.actionSuccessful("")
        }

        // Fail in getting user defaults
        every {
            database.getUserDefaults(any(), any())
        } answers {
            val listener = secondArg<ClientDatabaseService.UserDetailsListener>()
            listener.onError("")
        }

        every { auth.getCurrentUser() } returns AuthUser("", "", "")
        // Succeed updating FCM token
        every {
            database.updateUserFcmToken(any(), any())
        } answers {
            val listener = secondArg<WriteListener>()
            listener.onSuccess()
        }

        presenter.signIn("", "")

        verify { view.displayMessage(any()) }
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_success() {
        // Succeed sending email
        every {
            auth.sendPasswordResetEmail(any(), any())
        } answers {
            val listener = secondArg<AuthService.AuthActionListener>()
            listener.actionSuccessful("")
        }

        presenter.resetUserPassword("")

        verify {
            view.displayMessage(any())
        }
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_failed() {
        // Fail sending email
        every {
            auth.sendPasswordResetEmail(any(), any())
        } answers {
            val listener = secondArg<AuthService.AuthActionListener>()
            listener.actionFailed("")
        }

        presenter.resetUserPassword("")

        verify { view.displayMessage(any()) }
    }

}