package com.marknkamau.justjava.data.local

import androidx.room.*
import com.marknkamau.justjava.data.models.CartItem

@Dao
interface CartDao {
    @Insert
    suspend fun addItem(orderItem: CartItem)

    @Query("SELECT * FROM cart")
    suspend fun getAll(): MutableList<CartItem>

    @Query("SELECT SUM(itemPrice) from cart")
    suspend fun getTotal(): String

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("DELETE FROM cart")
    suspend fun deleteAll()

    @Update
    suspend fun updateItem(item: CartItem)
}
