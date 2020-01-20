package com.marknkamau.justjava.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.ProductsRepository
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

class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var productsRepository: ProductsRepository

    private lateinit var viewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = MainViewModel(productsRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can load products`() {
        val products = listOf<Product>()
        val resource = Resource.Success(products)
        coEvery { productsRepository.getProducts() } returns resource

        val observer = spyk<Observer<Resource<List<Product>>>>()
        viewModel.products.observeForever(observer)
        viewModel.getProducts()

        verify { observer.onChanged(resource) }
    }
}