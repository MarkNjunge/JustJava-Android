package com.marknjunge.core.model

data class OrderItem(val id: Int,
                     var itemName: String,
                     var itemQty: Int,
                     var itemCinnamon: Boolean,
                     var itemChoc: Boolean,
                     var itemMarshmallow: Boolean,
                     var itemPrice: Int)