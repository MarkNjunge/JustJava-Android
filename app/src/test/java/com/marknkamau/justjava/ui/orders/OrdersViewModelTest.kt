package com.marknkamau.justjava.ui.orders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
import com.marknkamau.justjava.utils.SampleData
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

class OrdersViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var ordersRepository: OrdersRepository

    private lateinit var viewModel: OrdersViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = OrdersViewModel(ordersRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can get orders`() {
        val resource = Resource.Success(listOf(SampleData.order))
        coEvery { ordersRepository.getOrders() } returns resource

        val observer = spyk<Observer<Resource<List<Order>>>>()
        viewModel.orders.observeForever(observer)
        viewModel.getOrders()

        verify { observer.onChanged(resource) }
    }
}