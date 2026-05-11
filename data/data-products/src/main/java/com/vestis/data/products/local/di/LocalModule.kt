package com.vestis.data.products.local.di

import android.content.Context
import com.vestis.data.products.local.dao.ProductDao
import com.vestis.data.products.local.db.ProductRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideProductRoomDatabase(
        @ApplicationContext context: Context,
    ): ProductRoomDatabase = ProductRoomDatabase.createInstance(context)

    @Provides
    @Singleton
    fun provideProductDao(
        database: ProductRoomDatabase,
    ): ProductDao = database.productDAO()
}