package com.marknkamau.justjava.ui.cart

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.ui.BaseView

internal interface CartView : BaseView{
    fun setDisplayToLoggedIn(userDetails: UserDetails)
    fun setDisplayToLoggedOut()
    fun displayCart(orderItems: MutableList<CartItem>)
    fun displayEmptyCart()
    fun displayCartTotal(total: Int)
    fun showLoadingBar()
    fun hideLoadingBar()
    fun finishActivity(order: Order)
}
