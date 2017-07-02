package com.marknkamau.justjava.data

import com.marknkamau.justjava.models.CartItem
import io.realm.RealmResults

interface CartRepository{
    fun getAllCartItems() : MutableList<CartItem>

    fun getTotalPrice() : Int

    fun saveNewItem(cartItem: CartItem)

    fun deleteAllItems()

    fun deleteSingleItem(item: CartItem)

    fun saveEdit(item: CartItem)
}
