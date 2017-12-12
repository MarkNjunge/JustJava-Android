package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.ui.BaseView

internal interface CartView : BaseView{
    fun displayCart(cartItems: MutableList<CartItem>)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
