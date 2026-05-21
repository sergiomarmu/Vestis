package com.vestis.domain.favorite.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteIdsFlowUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke() = favoriteRepository
        .getFavoriteIdsFlow()
}

