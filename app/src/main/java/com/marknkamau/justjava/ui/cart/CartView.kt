package com.marknkamau.justjava.ui.cart

import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.ui.BaseView

internal interface CartView : BaseView{
    fun displayCart(orderItems: MutableList<OrderItem>)
    fun displayEmptyCart()
    fun displayCartTotal(totalCost: Int)
}
