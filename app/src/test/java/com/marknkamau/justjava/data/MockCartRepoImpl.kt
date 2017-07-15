package com.marknkamau.justjava.data

import com.marknkamau.justjava.models.CartItem
import java.util.*

object MockCartRepoImpl : CartRepository{
    override fun getAllCartItems(): MutableList<CartItem> {
        return Arrays.asList(CartItem())
    }

    override fun getTotalPrice(): Int {
        return 0
    }

    override fun saveNewItem(cartItem: CartItem) {

    }

    override fun deleteAllItems() {

    }

    override fun deleteSingleItem(item: CartItem) {

    }

    override fun saveEdit(item: CartItem) {

    }

}