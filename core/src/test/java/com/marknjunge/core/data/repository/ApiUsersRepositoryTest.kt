package com.marknjunge.core.data.repository

import com.marknjunge.core.SampleData
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
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

    private lateinit var repo: UsersRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = ApiUsersRepository(usersService, preferencesRepository)
    }

    // updateUser
    @Test
    fun `verify updateUser runs and saves user`() = runBlocking {
        val user = SampleData.user
        val updatedUser = user.copy(firstName = "newFName")
        coEvery { usersService.updateUser(any(), any()) } just runs
        coEvery { preferencesRepository.user } returns user

        val resource = repo.updateUser("newFName", user.lastName, user.mobileNumber!!, user.email)

        coVerify { usersService.updateUser(any(), any()) }
        coVerify { preferencesRepository.user = updatedUser }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify updateUser handles error`() = runBlocking {
        coEvery { usersService.updateUser(any(), any()) } throws Exception("error")

        val resource = repo.updateUser("", "", "", "")

        coVerify { usersService.updateUser(any(), any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }

    // updateFcmToken
    @Test
    fun `verify updateFcmToken runs and saves user`() = runBlocking {
        coEvery { usersService.updateFcmToken(any(), any()) } just runs
        coEvery { preferencesRepository.user } returns SampleData.user

        val resource = repo.updateFcmToken("new_token")

        coVerify { usersService.updateFcmToken(any(), any()) }
        coVerify { preferencesRepository.user = SampleData.user.copy(fcmToken = "new_token") }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify updateFcmToken handles error`() = runBlocking {
        coEvery { usersService.updateFcmToken(any(), any()) } throws Exception("")

        val resource = repo.updateFcmToken("")

        coVerify { usersService.updateFcmToken(any(), any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }
}