package com.marknkamau.justjava.data.db

import com.marknkamau.justjava.data.models.AppProduct
import com.marknkamau.justjava.data.models.CartItem

interface DbRepository {
    suspend fun saveItemToCart(product: AppProduct, quantity: Int)

    suspend fun getCartItems(): List<CartItem>

    suspend fun deleteItemFromCart(item: CartItem)

    suspend fun clearCart()
}
