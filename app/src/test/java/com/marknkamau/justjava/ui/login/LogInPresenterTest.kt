package com.marknkamau.justjava.ui.login

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.model.AuthUser
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.data.preferences.PreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

class LogInPresenterTest {

    @MockK
    private lateinit var view: LogInView
    @MockK
    private lateinit var preferences: PreferencesRepository
    @MockK
    private lateinit var auth: AuthService
    @MockK
    private lateinit var userService: UserService

    private lateinit var presenter: LogInPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = LogInPresenter(view, preferences, auth, userService, Dispatchers.Unconfined)
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
        coEvery {
            auth.signIn(any(), any())
        } returns ""

        // Succeed in getting user defaults
        coEvery {
            userService.getUserDetails(any())
        } returns UserDetails("", "", "", "", "")

        every { auth.getCurrentUser() } returns AuthUser("", "", "")

        // Succeed updating FCM token
        coEvery{
            userService.updateUserFcmToken(any())
        }  returns Unit

        presenter.signIn("", "")

        verify { view.finishSignIn() }
    }

    @Test
    fun should_displayMessage_when_signIn_failed() {
        // Fail in signing in
        coEvery {
            auth.signIn(any(), any())
        } throws Exception("")

        presenter.signIn("", "")

        verify { view.displayMessage(any()) }
    }

    @Test
    fun should_displayMessage_when_getUserDefaults_failed() {
        // Succeed in signing in
        coEvery {
            auth.signIn(any(), any())
        } returns ""

        // Fail in getting user defaults
        coEvery{
            userService.getUserDetails(any())
        } throws Exception("")


        every { auth.getCurrentUser() } returns AuthUser("", "", "")
        // Succeed updating FCM token
        coEvery {
            userService.updateUserFcmToken(any())
        } returns Unit

        presenter.signIn("", "")

        verify { view.displayMessage(any()) }
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_success() {
        // Succeed sending email
        coEvery {
            auth.sendPasswordResetEmail(any())
        } returns Unit

        presenter.resetUserPassword("")

        verify {
            view.displayMessage(any())
        }
    }

    @Test
    fun should_displayMessage_when_sendPasswordRestEmail_failed() {
        // Fail sending email
        coEvery {
            auth.sendPasswordResetEmail(any())
        } throws Exception("")

        presenter.resetUserPassword("")

        verify { view.displayMessage(any()) }
    }

}