package com.marknkamau.justjava.ui.payMpesa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.PaymentsRepository
import com.marknkamau.justjava.utils.SampleData
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

class PayMpesaViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK
    private lateinit var paymentsRepository: PaymentsRepository

    private lateinit var viewModel: PayMpesaViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = PayMpesaViewModel(preferencesRepository, paymentsRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can get user`() {
        every { preferencesRepository.user } returns SampleData.user

        Assert.assertEquals(SampleData.user, viewModel.getUser())
    }

    @Test
    fun `can initiate mpesa payment`() {
        val resource = Resource.Success(ApiResponse(""))
        coEvery { paymentsRepository.requestMpesa(any(), any()) } returns resource

        val observer = spyk<Observer<Resource<ApiResponse>>>()
        viewModel.payMpesa("", "").observeForever(observer)

        verify { observer.onChanged(resource) }
    }
}