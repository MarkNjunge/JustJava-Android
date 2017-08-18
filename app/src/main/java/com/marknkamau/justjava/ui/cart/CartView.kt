package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.models.CartItemRoom

internal interface CartView {
    fun displayCart(cartItems: MutableList<CartItemRoom>?)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
