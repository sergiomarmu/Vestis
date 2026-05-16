package com.vestis.data.favorite.local.di

import com.vestis.data.favorite.local.datasource.FavoriteLocalDataSource
import com.vestis.data.favorite.local.datasource.FavoriteLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteDataLocalSourceModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteLocalDataSource(
        favoriteLocalDataSource: FavoriteLocalDataSourceImpl
    ): FavoriteLocalDataSource
}