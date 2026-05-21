package com.vestis.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vestis.core.data.local.room.dao.FavoriteDao
import com.vestis.core.data.local.room.dao.ProductDao
import com.vestis.core.data.local.room.entity.FavoriteEntity
import com.vestis.core.data.local.room.entity.ProductEntity

@Database(
    version = AppRoomDatabase.DATABASE_VERSION,
    entities = [
        ProductEntity::class,
        FavoriteEntity::class,
    ]
)
abstract class AppRoomDatabase : RoomDatabase() {
    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "vestis_db"

        fun createInstance(
            applicationContext: Context,
        ) = Room
            .databaseBuilder(
                context = applicationContext,
                klass = AppRoomDatabase::class.java,
                name = DATABASE_NAME
            )
            .build()
    }

    abstract fun productDAO(): ProductDao
    abstract fun favoriteDAO(): FavoriteDao
}