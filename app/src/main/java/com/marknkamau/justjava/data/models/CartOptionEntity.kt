package com.marknkamau.justjava.data.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "cart_product_options",
    foreignKeys = [ForeignKey(
        entity = CartProductEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cart_products_row_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["cart_products_row_id"])]
)
@Parcelize
data class CartOptionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "choice_id")
    var choiceId: Long,

    @ColumnInfo(name = "choice_name")
    var choiceName: String,

    @ColumnInfo(name = "option_id")
    var optionId: Long,

    @ColumnInfo(name = "option_name")
    var optionName: String,

    @ColumnInfo(name = "option_price")
    var optionPrice: Double,

    @ColumnInfo(name = "cart_products_row_id")
    var cartProductsRowId: Long
) : Parcelable
