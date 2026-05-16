package com.vestis.data.products.network.di

import com.vestis.data.products.network.datasource.ProductNetworkDataSource
import com.vestis.data.products.network.datasource.ProductNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsNetworkDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindProductNetworkDataSource(
        productNetworkDataSource: ProductNetworkDataSourceImpl
    ): ProductNetworkDataSource
}