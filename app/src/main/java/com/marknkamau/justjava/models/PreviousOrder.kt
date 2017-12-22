package com.marknkamau.justjava.models

import java.util.*

class PreviousOrder(val orderId: String,
                    val itemsCount: Int,
                    val deliveryAddress: String,
                    val timestamp: Date,
                    val totalPrice: Int,
                    val status: String)
