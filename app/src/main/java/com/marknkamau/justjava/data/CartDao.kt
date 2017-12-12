package com.marknkamau.justjava.data

import android.arch.persistence.room.*
import com.marknkamau.justjava.models.CartItem
import io.reactivex.Single

@Dao
interface CartDao{
    @Insert
    fun addItem(cartItem: CartItem)

    @Query("SELECT * FROM cart")
    fun getAll() : Single<MutableList<CartItem>>

    @Query("SELECT SUM(itemPrice) from cart")
    fun getTotal() : Single<String>

    @Delete
    fun deleteItem(item: CartItem)

    @Query("DELETE FROM cart")
    fun deleteAll()

    @Update
    fun updateItem(item: CartItem)

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX(itemName) FROM cart) WHERE name='cart'")
    fun resetIndex()
}
