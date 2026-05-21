package com.vestis.core.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vestis.core.data.local.room.entity.ProductEntity
import com.vestis.core.data.local.room.entity.ProductEntity.Companion.TABLE_NAME
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