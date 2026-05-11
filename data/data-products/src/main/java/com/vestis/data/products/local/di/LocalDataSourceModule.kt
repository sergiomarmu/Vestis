package com.vestis.data.products.local.di

import com.vestis.data.products.local.datasource.ProductLocalDataSource
import com.vestis.data.products.local.datasource.ProductLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductDataLocalSourceModule {

    @Binds
    @Singleton
    abstract fun bindProductLocalDataSource(
        productLocalDataSource: ProductLocalDataSourceImpl
    ): ProductLocalDataSource
}