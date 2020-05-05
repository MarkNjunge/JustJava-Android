package com.marknkamau.justjava.data.db

import com.marknkamau.justjava.data.models.AppProduct
import com.marknkamau.justjava.data.models.CartOptionEntity
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.data.models.CartProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbRepositoryImpl(private val cartDao: CartDao) : DbRepository {
    override suspend fun saveItemToCart(product: AppProduct, quantity: Int) = withContext(Dispatchers.IO) {
        val cartProductId = cartDao.addItem(
            CartProductEntity(
                0,
                product.id,
                product.name,
                product.price,
                product.calculateTotal(quantity),
                quantity
            )
        )

        product.choices?.forEach { choice ->
            choice.options.filter { it.isChecked }.forEach { option ->
                val optionEntity = CartOptionEntity(
                    0,
                    choice.id.toLong(),
                    choice.name,
                    option.id.toLong(),
                    option.name,
                    option.price,
                    cartProductId
                )
                cartDao.addItem(optionEntity)
            }
        }

        Unit
    }

    override suspend fun getCartItems(): List<CartItem> = withContext(Dispatchers.IO) {
        cartDao.getAllWithOptions()
    }

    override suspend fun deleteItemFromCart(item: CartItem) = withContext(Dispatchers.IO) {
        cartDao.deleteItem(item.cartItem)
    }

    override suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.deleteAll()
    }
}
