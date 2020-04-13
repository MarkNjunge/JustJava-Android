package com.marknjunge.core.data.repository

import com.marknjunge.core.SampleData
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.network.service.ApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class ApiProductsRepositoryTest {

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var repo: ProductsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repo = ApiProductsRepository(apiService)
    }

    @Test
    fun `verify getProducts runs`() = runBlocking {
        coEvery { apiService.getProducts() } returns listOf(SampleData.product)

        val resource = repo.getProducts()

        coVerify { apiService.getProducts() }
        Assert.assertTrue(resource is Resource.Success)
    }

    @Test
    fun `verify getProducts handles error`() = runBlocking {
        coEvery { apiService.getProducts() } throws Exception("error")

        val resource = repo.getProducts()

        coVerify { apiService.getProducts() }
        Assert.assertTrue(resource is Resource.Failure)
    }
}