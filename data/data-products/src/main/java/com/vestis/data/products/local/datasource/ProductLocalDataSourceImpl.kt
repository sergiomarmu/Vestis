package com.vestis.data.products.local.datasource

import com.vestis.core.data.local.room.dao.ProductDao
import com.vestis.core.data.local.room.entity.ProductEntity
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductLocalDataSource {

    override suspend fun upsertAll(
        products: List<ProductEntity>
    ) {
        productDao.upsertAll(
            products = products
        )
    }

    override fun observeAll() = productDao
        .observeAll()

    override suspend fun getCount() = productDao
        .getCount()
}