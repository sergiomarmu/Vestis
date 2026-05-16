package com.vestis.data.favorite.local.di

import android.content.Context
import com.vestis.data.favorite.local.dao.FavoriteDao
import com.vestis.data.favorite.local.db.FavoriteRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteLocalDatabaseModule {

    @Provides
    @Singleton
    fun provideFavoriteRoomDatabase(
        @ApplicationContext context: Context,
    ): FavoriteRoomDatabase = FavoriteRoomDatabase.createInstance(context)

    @Provides
    @Singleton
    fun provideProductDao(
        database: FavoriteRoomDatabase,
    ): FavoriteDao = database.favoriteDAO()
}