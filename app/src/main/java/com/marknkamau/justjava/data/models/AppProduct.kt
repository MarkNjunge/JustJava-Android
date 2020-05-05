package com.marknkamau.justjava.data.models

import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.ProductChoice
import com.marknjunge.core.data.model.ProductChoiceOption

data class AppProduct(
    val id: Long,
    val name: String,
    val slug: String,
    val image: String,
    val createdAt: Long,
    val price: Double,
    val description: String,
    val type: String,
    var choices: List<AppProductChoice>?,
    val status: String
) {
    fun calculateTotal(quantity: Int): Double {
        val basePrice = price

        val optionsTotal = choices?.fold(0.0) { i, c ->
            i + c.options
                .filter { it.isChecked }
                .fold(0.0) { acc, o -> acc + o.price }
        } ?: 0.0

        return (basePrice + optionsTotal) * quantity
    }

    fun validate(): MutableList<String> {
        val errors = mutableListOf<String>()
        choices?.forEach { choice ->
            if (choice.isRequired && !choice.hasValue) {
                errors.add(choice.name)
            }
        }

        return errors
    }
}

data class AppProductChoice(
    val id: Int,
    val name: String,
    val position: Int,
    val qtyMax: Int,
    val qtyMin: Int,
    var options: List<AppProductChoiceOption>
) {
    val isRequired: Boolean = qtyMin == 1
    val isSingleSelectable = qtyMax == 1
    val hasValue: Boolean
        get() = options.any { it.isChecked }
}

data class AppProductChoiceOption(
    val id: Int,
    val price: Double,
    val name: String,
    val description: String?,
    var isChecked: Boolean = false
)

fun Product.toAppModel() =
    AppProduct(
        id,
        name,
        slug,
        image,
        createdAt,
        price,
        description,
        type,
        choices?.toAppChoice(),
        status
    )

private fun List<ProductChoice>.toAppChoice(): List<AppProductChoice> = this.map {
    AppProductChoice(
        it.id,
        it.name,
        it.position,
        it.qtyMax,
        it.qtyMin,
        it.options.toAppOption()
    )
}.sortedBy { it.id }

private fun List<ProductChoiceOption>.toAppOption(): List<AppProductChoiceOption> = map {
    AppProductChoiceOption(
        it.id,
        it.price,
        it.name,
        it.description
    )
}.sortedBy { it.price }
