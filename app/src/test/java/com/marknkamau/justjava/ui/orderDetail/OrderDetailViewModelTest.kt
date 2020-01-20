package com.marknkamau.justjava.ui.orderDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
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

class OrderDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var ordersRepository: OrdersRepository

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    private lateinit var viewModel: OrderDetailViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = OrderDetailViewModel(ordersRepository, preferencesRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can get order`() {
        val resource = Resource.Success(SampleData.order)
        coEvery { ordersRepository.getOrderById(any()) } returns resource

        val observer = spyk<Observer<Resource<Order>>>()
        viewModel.order.observeForever(observer)
        viewModel.getOrder("")

        verify { observer.onChanged(resource) }
    }

    @Test
    fun `can get user`() {
        every { preferencesRepository.user } returns SampleData.user

        Assert.assertEquals(SampleData.user, viewModel.getUser())
    }
}