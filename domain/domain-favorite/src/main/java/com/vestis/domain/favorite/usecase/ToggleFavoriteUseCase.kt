package com.vestis.domain.favorite.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(
        productId: Int
    ) {
        favoriteRepository.toggleFavorite(
            productId = productId
        )
    }
}