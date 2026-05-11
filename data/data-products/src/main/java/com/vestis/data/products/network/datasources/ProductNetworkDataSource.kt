package com.vestis.data.products.network.datasources

import com.vestis.core.common.either.Either
import com.vestis.core.data.exception.DataException
import com.vestis.data.products.network.dto.ProductDTO

interface ProductNetworkDataSource {
    suspend fun getProducts(): Either<DataException, List<ProductDTO>>
}