package com.vestis.domain.products.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import com.vestis.domain.products.repository.ProductRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProductsFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(
        forceNetwork: Boolean
    ) = productRepository.getProductsFlow(
        forceNetwork = forceNetwork
    ).combine(
        flow = favoriteRepository.getFavoriteIdsFlow()
    ) { products, favIds ->
        products.map { product ->
            product.copy(isFavorite = product.id in favIds)
        }
    }
}