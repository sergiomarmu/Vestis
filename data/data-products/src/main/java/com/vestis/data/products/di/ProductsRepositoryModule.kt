package com.vestis.data.products.di

import com.vestis.data.products.repository.ProductRepositoryImpl
import com.vestis.domain.products.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepository: ProductRepositoryImpl
    ): ProductRepository
}