package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.models.CartItem

internal interface CartActivityView {
    fun displayCart(cartItems: MutableList<CartItem>)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
