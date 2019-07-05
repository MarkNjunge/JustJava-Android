package com.marknkamau.justjava.ui.viewOrder

import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface ViewOrderView : BaseView {
    fun displayOrder(order: Order)
    fun displayOrderItems(orderItems: List<OrderItem>)
}