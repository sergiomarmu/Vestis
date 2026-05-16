package com.vestis.data.products.repository

import com.vestis.core.data.exception.toExceptionDomain
import com.vestis.data.products.local.datasource.ProductLocalDataSource
import com.vestis.data.products.local.mapper.toDomain
import com.vestis.data.products.local.mapper.toProductEntity
import com.vestis.data.products.network.datasources.ProductNetworkDataSource
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productNetworkDataSource: ProductNetworkDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
) : ProductRepository {

    companion object {
        // Categories excluded to maintain a clothing-only catalog.
        private val EXCLUDED_CATEGORIES = setOf("electronics", "jewelery")
    }

    override fun getProductsFlow(
        forceNetwork: Boolean,
    ): Flow<List<ProductModel>> = flow {
        val productCount = productLocalDataSource.getCount()
        val isDbEmpty = productCount == 0

        if (forceNetwork || isDbEmpty) {
            productNetworkDataSource.getProducts()
                .fold(
                    ifLeft = {
                        if (forceNetwork && isDbEmpty) {
                            throw it.toExceptionDomain()
                        }
                    },
                    ifRight = { products ->
                        productLocalDataSource.upsertAll(
                            products
                                .filter { it.category !in EXCLUDED_CATEGORIES }
                                .toProductEntity()
                        )
                    }
                )
        }

        // This forces the Loading state to show for both API calls and local DB reads.
        // Only for development/review purposes.
        delay(timeMillis = 3_000L)

        emitAll(
            productLocalDataSource.observeAll()
                .map { it.toDomain() }
        )
    }
}