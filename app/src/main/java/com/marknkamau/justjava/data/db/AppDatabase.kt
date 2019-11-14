package com.marknkamau.justjava.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marknkamau.justjava.data.models.CartOptionEntity
import com.marknkamau.justjava.data.models.CartProductEntity

@Database(
    entities = [CartProductEntity::class, CartOptionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
