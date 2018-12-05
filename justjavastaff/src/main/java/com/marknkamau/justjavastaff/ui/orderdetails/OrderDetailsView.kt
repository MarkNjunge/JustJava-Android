package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.OrderStatus
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjavastaff.ui.BaseView

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface OrderDetailsView : BaseView {
    fun displayOrderItems(items: MutableList<OrderItem>)

    fun setUserDetails(user: UserDetails)

    fun setOrderStatus(status: OrderStatus)
}