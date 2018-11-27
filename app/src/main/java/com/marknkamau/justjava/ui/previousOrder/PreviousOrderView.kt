package com.marknkamau.justjava.ui.previousOrder

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface PreviousOrderView : BaseView {
    fun displayOrder(order: Order)
    fun displayOrderItems(orderItems: List<OrderItem>)
}