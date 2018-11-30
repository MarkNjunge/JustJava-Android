package com.marknkamau.justjava.data.local

import android.arch.persistence.room.*
import com.marknkamau.justjava.data.models.CartItem
import io.reactivex.Single

@Dao
interface CartDao{
    @Insert
    fun addItem(orderItem: CartItem)

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
}
