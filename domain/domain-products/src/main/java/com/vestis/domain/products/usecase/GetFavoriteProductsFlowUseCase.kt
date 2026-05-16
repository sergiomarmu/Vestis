package com.vestis.domain.products.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import com.vestis.domain.products.repository.ProductRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFavoriteProductsFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke() = productRepository.getProductsFlow(
        forceNetwork = false
    ).combine(
        flow = favoriteRepository.getFavoriteIdsFlows()
    ) { products, favIds ->
        products
            .filter { product -> product.id in favIds }
            .map { it.copy(isFavorite = true) }
    }
}