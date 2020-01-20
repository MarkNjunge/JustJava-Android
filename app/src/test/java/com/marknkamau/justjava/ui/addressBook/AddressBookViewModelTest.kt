package com.marknkamau.justjava.ui.addressBook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.utils.SampleData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AddressBookViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var viewModel: AddressBookViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = AddressBookViewModel(usersRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `can get addresses`() {
        val resource = Resource.Success(SampleData.user)
        coEvery { usersRepository.getCurrentUser() } returns flow { emit(resource) }

        val observer = spyk<Observer<Resource<User>>>()
        viewModel.user.observeForever(observer)
        viewModel.getAddresses()

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

    @Test
    fun `can delete address`() {
        val resource = Resource.Success(Unit)
        coEvery { usersRepository.deleteAddress(any()) } returns resource

        val observer = spyk<Observer<Resource<Unit>>>()
        viewModel.deleteAddress(SampleData.address).observeForever(observer)

        verify { observer.onChanged(resource) }
    }

}
