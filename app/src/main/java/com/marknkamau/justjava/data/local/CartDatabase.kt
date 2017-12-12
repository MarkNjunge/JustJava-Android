package com.marknkamau.justjava.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marknkamau.justjava.models.CartItem

@Database(entities = arrayOf(CartItem::class), version = 1)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDao(): CartDao
}
