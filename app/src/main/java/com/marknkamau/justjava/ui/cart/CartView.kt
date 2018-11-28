package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BaseView

internal interface CartView : BaseView{
    fun displayCart(orderItems: MutableList<CartItem>)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
