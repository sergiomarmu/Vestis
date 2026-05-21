package com.vestis.data.favorite.local.datasource

import com.vestis.core.data.local.room.dao.FavoriteDao
import com.vestis.core.data.local.room.entity.FavoriteEntity
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteLocalDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteLocalDataSource {
    override fun observeAll() = favoriteDao.observeAll()
        .map { list ->
            list.map { it.productId }
        }

    override suspend fun insert(
        productId: Int
    ) {
        favoriteDao.insert(
            favorite = FavoriteEntity(
                productId = productId
            )
        )
    }

    override suspend fun delete(
        productId: Int) {
        favoriteDao.delete(
            productId = productId
        )
    }

    override suspend fun exists(
        productId: Int
    ) = favoriteDao.exists(
        productId = productId
    )
}