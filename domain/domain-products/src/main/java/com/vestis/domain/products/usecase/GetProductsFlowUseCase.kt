package com.vestis.domain.products.usecase

import com.vestis.domain.products.repository.ProductRepository
import javax.inject.Inject

class GetProductsFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        forceNetwork: Boolean
    ) = productRepository.getProducts(
        forceNetwork = forceNetwork
    )
}