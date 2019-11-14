package com.marknkamau.justjava.data.db

import androidx.room.*
import com.marknkamau.justjava.data.models.CartOptionEntity
import com.marknkamau.justjava.data.models.CartProductEntity
import com.marknkamau.justjava.data.models.CartItem

@Dao
interface CartDao {
    @Insert
    suspend fun addItem(orderItem: CartProductEntity): Long

    @Insert
    suspend fun addItem(option: CartOptionEntity): Long

    @Query("SELECT * FROM cart_products")
    suspend fun getAllWithOptions(): List<CartItem>

    @Query("SELECT SUM(total_price) from cart_products")
    suspend fun getTotal(): String

    @Delete
    suspend fun deleteItem(item: CartProductEntity)

    @Query("DELETE FROM cart_products")
    suspend fun deleteAll()

    @Update
    suspend fun updateItem(item: CartProductEntity)
}
