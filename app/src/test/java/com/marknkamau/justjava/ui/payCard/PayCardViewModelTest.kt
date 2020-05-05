package com.marknkamau.justjava.ui.payCard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.PaymentsRepository
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

class PayCardViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var paymentsRepository: PaymentsRepository

    private lateinit var viewModel: PayCardViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = PayCardViewModel(paymentsRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can initiate card payment`() {
        val resource = Resource.Success(ApiResponse(""))
        coEvery {
            paymentsRepository.initiateCardPayment(
                any(),
                any()
            )
        } returns resource

        val observer = spyk<Observer<Resource<ApiResponse>>>()
        viewModel.initiateCardPayment("", "", "", "", "").observeForever(observer)

        verify { observer.onChanged(resource) }
    }
}