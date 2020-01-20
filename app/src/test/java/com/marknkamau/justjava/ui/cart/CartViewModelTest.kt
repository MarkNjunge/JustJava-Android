package com.marknkamau.justjava.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.VerifyOrderResponse
import com.marknjunge.core.data.repository.CartRepository
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.addressBook.AddressBookViewModel
import com.marknkamau.justjava.utils.SampleData
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.TestRule

class CartViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository
    @MockK
    private lateinit var dbRepository: DbRepository
    @MockK
    private lateinit var cartRepository: CartRepository

    private lateinit var viewModel: CartViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel(preferencesRepository, dbRepository, cartRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can get sign in status`() {
        every { preferencesRepository.isSignedIn } returns true

        assertEquals(true, viewModel.isSignedIn())
    }

    @Test
    fun `can get cart items`() {
        coEvery { dbRepository.getCartItems() } returns SampleData.cartItems

        val observer = spyk<Observer<List<CartItem>>>()
        viewModel.items.observeForever(observer)
        viewModel.getCartItems()

        verify { observer.onChanged(SampleData.cartItems) }
    }

    @Test
    fun `can delete cart item`() {
        coEvery { dbRepository.deleteItemFromCart(any()) } just runs
        coEvery { dbRepository.getCartItems() } returns SampleData.cartItems

        val observer = spyk<Observer<List<CartItem>>>()
        viewModel.items.observeForever(observer)
        viewModel.deleteItem(SampleData.cartItem)

        coVerify { dbRepository.deleteItemFromCart(any()) }
        verify { observer.onChanged(SampleData.cartItems) }
    }

    @Test
    fun `can verify order`() {
        val resource = Resource.Success(listOf(SampleData.verifyOrderResponse))
        coEvery { cartRepository.verifyOrder(any()) } returns resource

        val observer = spyk<Observer<Resource<List<VerifyOrderResponse>>>>()
        viewModel.verifyOrder(SampleData.cartItems).observeForever(observer)

        verify { observer.onChanged(resource) }
    }
}