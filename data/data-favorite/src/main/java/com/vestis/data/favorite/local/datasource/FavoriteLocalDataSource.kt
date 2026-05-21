package com.vestis.data.favorite.local.datasource

import com.vestis.core.data.local.room.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource {
    fun observeAll(): Flow<List<Int>>

    suspend fun insert(productId: Int)

    suspend fun delete(productId: Int)

    suspend fun exists(productId: Int): Boolean
}