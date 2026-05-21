package com.vestis.data.products.local.datasource

import com.vestis.core.data.local.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {

    suspend fun upsertAll(
        products: List<ProductEntity>
    )

    fun observeAll(): Flow<List<ProductEntity>>

    suspend fun getCount(): Int
}