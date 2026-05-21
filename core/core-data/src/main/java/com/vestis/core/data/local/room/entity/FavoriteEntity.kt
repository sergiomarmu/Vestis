package com.vestis.core.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavoriteEntity.TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey val productId: Int
) {
    companion object {
        const val TABLE_NAME = "favorites"
    }
}