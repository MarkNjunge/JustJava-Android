package com.marknkamau.justjava

import androidx.test.core.app.ActivityScenario
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.testUtils.*
import com.marknkamau.justjava.ui.main.MainActivity
import io.mockk.coEvery
import io.mockk.just
import io.mockk.runs
import org.junit.Test

class SmokeTest {
    @Test
    fun canPerformAppFunctions() {
        setupMockResponses()

        ActivityScenario.launch(MainActivity::class.java)

        // Go to productDetails
        onViewWithId(R.id.rvProducts).clickRecyclerViewItem(0)

        // Add item to cart
        onViewWithId(R.id.btnAddToCart).click()

        // Go to cart
        onViewWithId(R.id.menu_cart).click()

        // Go to checkout
        onViewWithId(R.id.btnCheckout).click()

        // Place order
        onViewWithId(R.id.btnPlaceOrder).click()
    }

    private fun setupMockResponses() {
        coEvery { TestApp.mockProductsRepository.getProducts() } returns Resource.Success(listOf(SampleData.product))
        coEvery { TestApp.mockPreferencesRepository.isSignedIn } returns true
        coEvery { TestApp.mockPreferencesRepository.user } returns SampleData.user
        coEvery { TestApp.mockDbRepository.saveItemToCart(any(), any()) } just runs
        coEvery { TestApp.mockDbRepository.getCartItems() } returns SampleData.cartItems
        coEvery { TestApp.mockCartRepository.verifyOrder(any()) } returns Resource.Success(listOf(SampleData.verifyOrderResponse))
        coEvery { TestApp.mockOrdersRepository.placeOrder(any()) } returns Resource.Success(SampleData.order)
        coEvery { TestApp.mockDbRepository.clearCart() } just runs
        coEvery { TestApp.mockOrdersRepository.getOrderById(any()) } returns Resource.Success(SampleData.order)
    }
}