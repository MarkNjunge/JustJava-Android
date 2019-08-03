package com.marknkamau.justjava.testUtils

import com.marknjunge.core.model.*

object TestData {
    val userId = "1"
    val email = "user@mail.com"
    val name = "User"
    val userDetails = UserDetails(userId, email, name, "254", "Nrb")
    val authUser = AuthUser(userId, email, name)
    val order = Order("", "", 1, 1, "", "", paymentMethod = "cash")
    val orders = listOf(order)
    val orderItems = listOf(OrderItem(0, "", 1, true, true, true, 0))
    val payments = listOf(Payment("", "", 0L, "", "", "", "", "", ""))

}