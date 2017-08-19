package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.models.CartItemRoom
import com.marknkamau.justjava.ui.BaseView

internal interface CartView : BaseView{
    fun displayCart(cartItems: MutableList<CartItemRoom>?)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
