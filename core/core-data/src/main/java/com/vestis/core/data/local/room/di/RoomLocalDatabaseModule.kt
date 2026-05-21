package com.vestis.core.data.local.room.di

import android.content.Context
import com.vestis.core.data.local.room.AppRoomDatabase
import com.vestis.core.data.local.room.dao.FavoriteDao
import com.vestis.core.data.local.room.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomLocalDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ): AppRoomDatabase = AppRoomDatabase.createInstance(
        applicationContext = context
    )

    @Provides
    @Singleton
    fun provideProductDao(
        database: AppRoomDatabase,
    ): ProductDao = database.productDAO()

    @Provides
    @Singleton
    fun provideFavoriteDao(
        database: AppRoomDatabase,
    ): FavoriteDao = database.favoriteDAO()
}