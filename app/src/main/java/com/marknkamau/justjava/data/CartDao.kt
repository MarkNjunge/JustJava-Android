package com.marknkamau.justjava.data

import android.arch.persistence.room.*
import com.marknkamau.justjava.models.CartItemRoom
import io.reactivex.Single

@Dao
interface CartDao{
    @Insert
    fun addItem(cartItem: CartItemRoom)

    @Query("SELECT * FROM cart")
    fun getAll() : Single<MutableList<CartItemRoom>>

    @Query("SELECT SUM(itemPrice) from cart")
    fun getTotal() : Single<String>

    @Delete
    fun deleteItem(item: CartItemRoom)

    @Query("DELETE FROM cart")
    fun deleteAll()

    @Update
    fun updateItem(item: CartItemRoom)

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX(itemName) FROM cart) WHERE name='cart'")
    fun resetIndex()
}
