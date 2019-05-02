package com.marknkamau.justjava.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.marknjunge.core.model.OrderItem
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cart")
@Parcelize
data class CartItem(@PrimaryKey(autoGenerate = true) val id: Int,
                    var itemName: String,
                    var itemQty: Int,
                    var itemCinnamon: Boolean,
                    var itemChoc: Boolean,
                    var itemMarshmallow: Boolean,
                    var itemPrice: Int) : Parcelable {

    fun toOrderItem(): OrderItem {
        return OrderItem(this.id, this.itemName, this.itemQty, this.itemCinnamon, this.itemChoc, this.itemMarshmallow, this.itemPrice)
    }
}
