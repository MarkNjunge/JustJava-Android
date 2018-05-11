package com.marknkamau.justjava.models

data class Order(val orderId: String,
                 val customerId: String,
                 var itemsCount: Int,
                 var totalPrice: Int,
                 val deliveryAddress: String,
                 val additionalComments: String)
