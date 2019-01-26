package com.marknkamau.justjava.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marknkamau.justjava.data.models.CartItem

@Database(entities = [(CartItem::class)], version = 1)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDao(): CartDao
}
