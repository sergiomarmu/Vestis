package com.vestis.data.favorite.repository

import com.vestis.data.favorite.local.datasource.FavoriteLocalDataSource
import com.vestis.domain.favorite.repository.FavoriteRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
) : FavoriteRepository {
    override fun getFavoriteIdsFlows() = favoriteLocalDataSource.observeAll()
        .map { it.toSet() }

    override suspend fun toggleFavorite(
        productId: Int
    ) {
        val exist = favoriteLocalDataSource.exists(productId = productId)

        if (exist) {
            favoriteLocalDataSource.delete(productId = productId)
        } else {
            favoriteLocalDataSource.insert(productId = productId)
        }
    }

    override suspend fun isFavorite(
        productId: Int
    ) = favoriteLocalDataSource.exists(productId = productId)
}