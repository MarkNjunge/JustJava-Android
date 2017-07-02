package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.models.CartItem

internal interface CartView {
    fun displayCart(cartItems: MutableList<CartItem>)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
