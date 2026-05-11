package com.vestis.data.products.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vestis.data.products.local.entity.ProductEntity
import com.vestis.data.products.local.entity.ProductEntity.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(
        products: List<ProductEntity>
    )

    @Query("SELECT * FROM $TABLE_NAME")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME")
    suspend fun getCount(): Int
}