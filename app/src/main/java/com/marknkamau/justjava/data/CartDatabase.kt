package com.marknkamau.justjava.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marknkamau.justjava.models.CartItemRoom

@Database(entities = arrayOf(CartItemRoom::class), version = 1)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDao(): CartDao
}
