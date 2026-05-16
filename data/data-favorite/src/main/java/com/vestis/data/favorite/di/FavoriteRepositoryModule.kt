package com.vestis.data.favorite.di

import com.vestis.data.favorite.repository.FavoriteRepositoryImpl
import com.vestis.domain.favorite.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepository: FavoriteRepositoryImpl
    ): FavoriteRepository
}