package com.vestis.data.products.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vestis.data.products.local.dao.ProductDao
import com.vestis.data.products.local.entity.ProductEntity

@Database(
    version = ProductRoomDatabase.DATABASE_VERSION,
    entities = [
        ProductEntity::class
    ]
)
abstract class ProductRoomDatabase : RoomDatabase() {
    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "vestis_db"

        fun createInstance(
            applicationContext: Context,
        ) = Room
            .databaseBuilder(
                context = applicationContext,
                klass = ProductRoomDatabase::class.java,
                name = DATABASE_NAME
            )
            .build()
    }

    abstract fun productDAO(): ProductDao
}