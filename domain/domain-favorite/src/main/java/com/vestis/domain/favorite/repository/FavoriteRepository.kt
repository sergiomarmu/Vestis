package com.vestis.domain.favorite.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteIdsFlows(): Flow<Set<Int>>

    suspend fun toggleFavorite(
        productId: Int
    )

    suspend fun isFavorite(
        productId: Int
    ): Boolean
}