package com.marknkamau.justjava

import androidx.test.core.app.ActivityScenario
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.testUtils.*
import com.marknkamau.justjava.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.just
import io.mockk.runs
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SmokeTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

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
        coEvery { TestRepositoriesModule.mockProductsRepository.getProducts() } returns Resource.Success(
            listOf(
                SampleData.product
            )
        )
        coEvery { TestRepositoriesModule.mockPreferencesRepository.isSignedIn } returns true
        coEvery { TestRepositoriesModule.mockPreferencesRepository.user } returns SampleData.user
        coEvery { TestRepositoriesModule.mockDbRepository.saveItemToCart(any(), any()) } just runs
        coEvery { TestRepositoriesModule.mockDbRepository.getCartItems() } returns SampleData.cartItems
        coEvery { TestRepositoriesModule.mockCartRepository.verifyOrder(any()) } returns Resource.Success(
            listOf(
                SampleData.verifyOrderResponse
            )
        )
        coEvery { TestRepositoriesModule.mockOrdersRepository.placeOrder(any()) } returns Resource.Success(SampleData.order)
        coEvery { TestRepositoriesModule.mockDbRepository.clearCart() } just runs
        coEvery { TestRepositoriesModule.mockOrdersRepository.getOrderById(any()) } returns Resource.Success(SampleData.order)
    }
}