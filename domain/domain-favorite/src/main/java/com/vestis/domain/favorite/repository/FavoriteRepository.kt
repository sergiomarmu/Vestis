package com.vestis.domain.favorite.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteIdsFlow(): Flow<Set<Int>>

    suspend fun toggleFavorite(
        productId: Int
    )

    suspend fun isFavorite(
        productId: Int
    ): Boolean
}