package com.marknjunge.core.data.repository

import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.VerifyOrderDto
import com.marknjunge.core.data.network.service.CartService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ApiCartRepositoryTest {

    @MockK
    private lateinit var cartService: CartService

    private lateinit var repo: ApiCartRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = ApiCartRepository(cartService)
    }

    @Test
    fun `verify verifyOrder runs`() = runBlocking {
        coEvery { cartService.verifyCart(any()) } returns listOf()

        val resource = repo.verifyOrder(VerifyOrderDto(listOf()))

        coVerify { cartService.verifyCart(any()) }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify verifyOrder handles error`() = runBlocking {
        coEvery { cartService.verifyCart(any()) } throws Exception("Error")

        val resource = repo.verifyOrder(VerifyOrderDto(listOf()))

        coVerify { cartService.verifyCart(any()) }
        Assert.assertTrue(resource is Resource.Failure)
    }
}