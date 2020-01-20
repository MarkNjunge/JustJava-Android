package com.marknkamau.justjava.ui.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.AppProduct
import io.mockk.*
import io.mockk.impl.annotations.MockK
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

class ProductDetailsViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var dbRepository: DbRepository

    private lateinit var viewModel: ProductDetailsViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = ProductDetailsViewModel(dbRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can add item to cart`() {
        coEvery { dbRepository.saveItemToCart(any(), any()) } just runs

        val observer = spyk<Observer<Unit>>()
        viewModel.addItemToCart(AppProduct(0L, "", "", "", 0L, 0.0, "", "", null, ""), 1).observeForever(observer)

        verify { observer.onChanged(Unit) }
        coVerify(exactly = 1) { dbRepository.saveItemToCart(any(), any()) }
    }
}