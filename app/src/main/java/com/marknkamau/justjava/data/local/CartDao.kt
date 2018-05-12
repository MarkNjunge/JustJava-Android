package com.marknkamau.justjava.data.local

import android.arch.persistence.room.*
import com.marknkamau.justjava.data.models.OrderItem
import io.reactivex.Single

@Dao
interface CartDao{
    @Insert
    fun addItem(orderItem: OrderItem)

    @Query("SELECT * FROM cart")
    fun getAll() : Single<MutableList<OrderItem>>

    @Query("SELECT SUM(itemPrice) from cart")
    fun getTotal() : Single<String>

    @Delete
    fun deleteItem(item: OrderItem)

    @Query("DELETE FROM cart")
    fun deleteAll()

    @Update
    fun updateItem(item: OrderItem)

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX(itemName) FROM cart) WHERE name='cart'")
    fun resetIndex()
}
