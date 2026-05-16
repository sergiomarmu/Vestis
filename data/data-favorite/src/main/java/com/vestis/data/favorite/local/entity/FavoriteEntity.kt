package com.vestis.data.favorite.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vestis.data.favorite.local.entity.FavoriteEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey val productId: Int
) {
    companion object {
        const val TABLE_NAME = "favorites"
    }
}