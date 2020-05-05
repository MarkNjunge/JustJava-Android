package com.marknkamau.justjava.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class CartItem(
    @Embedded val cartItem: CartProductEntity,
    @Relation(parentColumn = "id", entityColumn = "cart_products_row_id")
    var options: List<CartOptionEntity>
)
