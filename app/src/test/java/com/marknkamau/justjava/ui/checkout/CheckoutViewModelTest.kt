package com.marknkamau.justjava.ui.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.PaymentMethod
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.CartItem
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

class CheckoutViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository
    @MockK
    private lateinit var dbRepository: DbRepository
    @MockK
    private lateinit var ordersRepository: OrdersRepository
    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var viewModel: CheckoutViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = CheckoutViewModel(preferencesRepository, dbRepository, ordersRepository, usersRepository)
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

        Assert.assertEquals(true, viewModel.isSignedIn())
    }

    @Test
    fun `can get user`() {
        every { preferencesRepository.user } returns SampleData.user

        Assert.assertEquals(SampleData.user, viewModel.getUser())
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
    fun `can clear cart`() {
        coEvery { dbRepository.clearCart() } just runs

        viewModel.clearCart()

        coVerify { dbRepository.clearCart() }
    }

    @Test
    fun `can place order`() {
        val resource = Resource.Success(SampleData.order)
        coEvery { dbRepository.getCartItems() } returns SampleData.cartItems
        coEvery { ordersRepository.placeOrder(any()) } returns resource

        val observer = spyk<Observer<Resource<Order>>>()
        viewModel.placeOrder(PaymentMethod.MPESA, SampleData.address).observeForever(observer)

        verify { observer.onChanged(resource) }
    }

    @Test
    fun `can add address`() {
        val resource = Resource.Success(SampleData.address)
        coEvery { usersRepository.addAddress(any()) } returns resource

        val observer = spyk<Observer<Resource<Address>>>()
        viewModel.addAddress(SampleData.address).observeForever(observer)

        verify { observer.onChanged(resource) }
    }

}