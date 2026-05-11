package com.vestis.domain.products.repository

import com.vestis.domain.products.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(
        forceNetwork: Boolean = false
    ): Flow<List<ProductModel>>
}