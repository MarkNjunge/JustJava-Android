package com.marknjunge.core

import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Session
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.ProductChoice
import com.marknjunge.core.data.model.ProductChoiceOption

internal object SampleData {
    val address = Address(0, "Street", "instructions", "-1,1")
    val user = User(1, "fName", "lName", 0L, "254712345678", "contact@mail.com", "token", "PASSWORD", listOf(address))
    val session = Session("", 0L, 0)

    val productChoiceOption = ProductChoiceOption(0, 0.0, "name", "desc")
    val productChoice = ProductChoice(0, "choice", 0, 0, 0, listOf(productChoiceOption))
    val product = Product(
        0,
        "prod",
        "prod",
        "image",
        0L,
        0.0,
        "desc",
        "type",
        listOf(productChoice),
        "status"
    )
}