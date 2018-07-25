package com.marknkamau.justjava.ui.previousOrder

import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.ui.BaseView

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

interface PreviousOrderView : BaseView{
    fun displayOrderItems(orderItems: List<OrderItem>)
}