package com.vestis.data.products.network.datasource

import com.vestis.core.common.dispatcher.IoDispatcher
import com.vestis.core.data.network.handler.tryRequest
import com.vestis.data.products.network.api.ProductsApiService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductNetworkDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val productsApiService: ProductsApiService,
) : ProductNetworkDataSource {

    override suspend fun getProducts() = tryRequest(
        ioDispatcher = ioDispatcher
    ) {
        productsApiService.getProducts()
    }
}