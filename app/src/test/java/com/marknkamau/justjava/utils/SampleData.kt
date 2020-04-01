package com.marknkamau.justjava.utils

import com.marknjunge.core.data.model.*
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.data.models.CartOptionEntity
import com.marknkamau.justjava.data.models.CartProductEntity

internal object SampleData {
    val address = Address(0, "Street", "instructions", "-1,1")
    val user = User(1, "fName", "lName", 0L, "254712345678", "contact@mail.com", "token", "PASSWORD", listOf(address))

    val cartOptionEntity = CartOptionEntity(0, 0, "Choice", 0, "Option", 20.0, 0)
    val cartItem = CartItem(CartProductEntity(0L, 0L, "Product", 100.0, 100.0, 1), listOf(cartOptionEntity))
    val cartItems = listOf(cartItem)

    val verifyOrderItemOptionDto = VerifyOrderItemOptionDto(0, 0, 100.0)
    val verifyOrderItemDto = VerifyOrderItemDto(0, 0, 100.0, listOf(verifyOrderItemOptionDto))
    val verifyOrderDto = VerifyOrderDto(listOf(verifyOrderItemDto))

    val verifyOrderResponse = VerifyOrderResponse(0, "error", ErrorType.MISSING, ErrorModel.CHOICE, 0, 200.0)

    val orderItem = OrderItem(1, 0, 100.0, 100.0, "Product", listOf(OrderItemOption(0, "Choice", 0, 10.0, 0, "Option")))
    val order =
        Order(null, 0, 100.0, PaymentMethod.MPESA, "abc123", 0, listOf(orderItem), PaymentStatus.PAID, OrderStatus.PENDING, 0)
}