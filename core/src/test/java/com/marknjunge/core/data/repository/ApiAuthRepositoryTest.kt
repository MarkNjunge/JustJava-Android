package com.marknjunge.core.data.repository

import com.marknjunge.core.SampleData
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.SignInResponse
import com.marknjunge.core.data.network.service.AuthService
import com.marknjunge.core.data.network.service.GoogleSignInService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ApiAuthRepositoryTest {

    @MockK
    private lateinit var authService: AuthService

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK
    private lateinit var googleSignInService: GoogleSignInService

    private lateinit var repo: ApiAuthRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = ApiAuthRepository(authService, preferencesRepository, googleSignInService)
    }

    @Test
    fun `verify signInWithGoogle runs and saves user,sessionId`() = runBlocking {
        coEvery { authService.signInWithGoogle(any()) } returns SignInResponse(SampleData.user, SampleData.session)

        val resource = repo.signInWithGoogle("")

        coVerify { authService.signInWithGoogle(any()) }
        coVerify { preferencesRepository.user = SampleData.user }
        coVerify { preferencesRepository.sessionId = SampleData.session.sessionId }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify signInWithGoogle handles error`() = runBlocking {
        coEvery { authService.signInWithGoogle(any()) } throws Exception("Error")

        val resource = repo.signInWithGoogle("")

        coVerify { authService.signInWithGoogle(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }

    @Test
    fun `verify signUp runs and saves user,sessionId`() = runBlocking {
        coEvery { authService.signUp(any()) } returns SignInResponse(SampleData.user, SampleData.session)

        val resource = repo.signUp("", "", "", "")

        coVerify { authService.signUp(any()) }
        coVerify { preferencesRepository.user = SampleData.user }
        coVerify { preferencesRepository.sessionId = SampleData.session.sessionId }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify signUp handles error`() = runBlocking {
        coEvery { authService.signUp(any()) } throws Exception("Error")

        val resource = repo.signUp("", "", "", "")

        coVerify { authService.signUp(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }

    @Test
    fun `verify signIn runs and saves user,sessionId`() = runBlocking {
        coEvery { authService.signIn(any()) } returns SignInResponse(SampleData.user, SampleData.session)

        val resource = repo.signIn("", "")

        coVerify { authService.signIn(any()) }
        coVerify { preferencesRepository.user = SampleData.user }
        coVerify { preferencesRepository.sessionId = SampleData.session.sessionId }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify signIn handles error`() = runBlocking {
        coEvery { authService.signIn(any()) } throws Exception("Error")

        val resource = repo.signIn("", "")

        coVerify { authService.signIn(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }

}