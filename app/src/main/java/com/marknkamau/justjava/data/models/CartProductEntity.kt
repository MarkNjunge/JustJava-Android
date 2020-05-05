package com.marknkamau.justjava.data.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cart_products")
@Parcelize
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "product_id")
    var productId: Long,

    @ColumnInfo(name = "product_name")
    var productName: String,

    @ColumnInfo(name = "product_base_price")
    var productBasePrice: Double,

    @ColumnInfo(name = "total_price")
    var totalPrice: Double,

    @ColumnInfo(name = "quantity")
    var quantity: Int
) : Parcelable
