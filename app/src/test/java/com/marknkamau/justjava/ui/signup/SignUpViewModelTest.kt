package com.marknkamau.justjava.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.network.service.FirebaseService
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.repository.UsersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SignUpViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var usersRepository: UsersRepository

    @MockK
    private lateinit var firebaseService: FirebaseService

    private lateinit var viewModel: SignUpViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = SignUpViewModel(authRepository, usersRepository)
        Dispatchers.setMain(testDispatcher)

        coEvery { firebaseService.getFcmToken() } returns ""
        coEvery { usersRepository.updateFcmToken() } returns Resource.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can sign up`() {
        val resource = Resource.Success(User(1, "", "", 0, "", "", "", "", listOf()))
        coEvery { authRepository.signUp(any(), any(), any(), any()) } returns resource

        val observer = spyk<Observer<Resource<User>>>()
        viewModel.signUp("", "", "", "").observeForever(observer)

        verify { observer.onChanged(resource) }
    }

}