package com.marknjunge.core.data.repository

import com.marknjunge.core.SampleData
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.service.FirebaseService
import com.marknjunge.core.data.network.service.GoogleSignInService
import com.marknjunge.core.data.network.service.UsersService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ApiUsersRepositoryTest {

    @MockK
    private lateinit var usersService: UsersService

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK
    private lateinit var googleSignInService: GoogleSignInService

    @MockK
    private lateinit var firebaseService: FirebaseService

    private lateinit var repo: UsersRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = ApiUsersRepository(usersService, preferencesRepository, googleSignInService, firebaseService)
    }

    // updateUser
    @Test
    fun `verify updateUser runs and saves user`() = runBlocking {
        val user = SampleData.user
        val updatedUser = user.copy(firstName = "newFName")
        coEvery { usersService.updateUser(any()) } just runs
        coEvery { preferencesRepository.user } returns user

        val resource = repo.updateUser("newFName", user.lastName, user.mobileNumber!!, user.email)

        coVerify { usersService.updateUser(any()) }
        coVerify { preferencesRepository.user = updatedUser }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify updateUser handles error`() = runBlocking {
        coEvery { usersService.updateUser(any()) } throws Exception("error")

        val resource = repo.updateUser("", "", "", "")

        coVerify { usersService.updateUser(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }

    // updateFcmToken
    @Test
    fun `verify updateFcmToken runs and saves user`() = runBlocking {
        coEvery { usersService.updateFcmToken(any()) } just runs
        coEvery { preferencesRepository.user } returns SampleData.user
        coEvery { firebaseService.getFcmToken() } returns "new_token"

        val resource = repo.updateFcmToken()

        coVerify { usersService.updateFcmToken(any()) }
        coVerify { preferencesRepository.user = SampleData.user.copy(fcmToken = "new_token") }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify updateFcmToken handles error`() = runBlocking {
        coEvery { usersService.updateFcmToken(any()) } throws Exception("")
        coEvery { firebaseService.getFcmToken() } returns "new_token"

        val resource = repo.updateFcmToken()

        coVerify { usersService.updateFcmToken(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }
}