package com.vestis.data.products.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vestis.data.products.local.entity.ProductEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "price")
    val price: Float,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String
) {
    companion object {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = "id"
    }
}