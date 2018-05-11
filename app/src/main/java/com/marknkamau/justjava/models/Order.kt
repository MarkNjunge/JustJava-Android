package com.marknkamau.justjava.models

import java.util.*

data class Order(val orderId: String,
                 val customerId: String,
                 var itemsCount: Int,
                 var totalPrice: Int,
                 val deliveryAddress: String,
                 val additionalComments: String,
                 val status: OrderStatus = OrderStatus.PENDING,
                 val date:Date = Date())
