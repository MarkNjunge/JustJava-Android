package com.marknkamau.justjavastaff.ui.orderdetails

import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.ui.BaseView

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface OrderDetailsView : BaseView {
    fun displayOrderItems(items: MutableList<OrderItem>)

    fun setOrderStatus(status: OrderStatus)
}