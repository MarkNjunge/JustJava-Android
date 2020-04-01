package com.marknkamau.justjava.testUtils

import com.marknjunge.core.data.model.*
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.data.models.CartOptionEntity
import com.marknkamau.justjava.data.models.CartProductEntity

object SampleData {
    val address = Address(0, "Street", "instructions", "-1,1")
    val user = User(1, "fName", "lName", 0L, "254712345678", "contact@mail.com", "token", "PASSWORD", listOf(address))

    val productChoiceOption = ProductChoiceOption(0, 0.0, "Single", "A single shot of coffee")
    val productChoice = ProductChoice(0, "Single, double or triple", 0, 1, 0, listOf(productChoiceOption))
    val product = Product(
        1,
        "Americano",
        "americano",
        "https://res.cloudinary.com/marknjunge/justjava/products/americano.jpg",
        1574605132,
        120.0,
        "Italian espresso gets the American treatment; hot water fills the cup for a rich alternative to drip coffee.",
        "coffee",
        listOf(productChoice),
        "enabled"
    )

    val cartOptionEntity = CartOptionEntity(0, 0, "Single, double or triple", 0, "Single", 20.0, 0)
    val cartItem = CartItem(CartProductEntity(0L, 0L, "Americano", 120.0, 120.0, 1), listOf(cartOptionEntity))
    val cartItems = listOf(cartItem)

    val verifyOrderResponse = VerifyOrderResponse(0, "error", ErrorType.MISSING, ErrorModel.CHOICE, 0, 200.0)

    val orderItem = OrderItem(1, 1, 120.0, 120.0, "Americano", listOf(OrderItemOption(1, "Single, double or triple", 3, 0.0, 44, "Single")))
    val order =
        Order(null, 1579505466, 120.0, PaymentMethod.MPESA, "AGV7OBST", 1, listOf(orderItem), PaymentStatus.PAID, OrderStatus.PENDING, 0)
}