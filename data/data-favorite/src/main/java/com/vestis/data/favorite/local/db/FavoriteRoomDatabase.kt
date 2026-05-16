package com.vestis.data.favorite.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vestis.data.favorite.local.dao.FavoriteDao
import com.vestis.data.favorite.local.entity.FavoriteEntity

@Database(
    version = FavoriteRoomDatabase.DATABASE_VERSION,
    entities = [
        FavoriteEntity::class
    ]
)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "vestis_favorites_db"

        fun createInstance(
            applicationContext: Context,
        ) = Room
            .databaseBuilder(
                context = applicationContext,
                klass = FavoriteRoomDatabase::class.java,
                name = DATABASE_NAME
            )
            .build()
    }

    abstract fun favoriteDAO(): FavoriteDao
}