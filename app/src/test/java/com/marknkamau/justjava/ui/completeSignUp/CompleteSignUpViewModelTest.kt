package com.marknkamau.justjava.ui.completeSignUp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.UsersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CompleteSignUpViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var viewModel: CompleteSignUpViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = CompleteSignUpViewModel(usersRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `can complete sign up`() {
        val resource = Resource.Success(Unit)
        coEvery { usersRepository.updateUser(any(), any(), any(), any()) } returns resource

        val observer = spyk<Observer<Resource<Unit>>>()
        viewModel.completeSignUp("", "", "", "").observeForever(observer)

        verify { observer.onChanged(resource) }
    }
}