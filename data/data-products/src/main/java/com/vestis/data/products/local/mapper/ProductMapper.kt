package com.vestis.data.products.local.mapper

import com.vestis.data.products.local.entity.ProductEntity
import com.vestis.data.products.network.dto.ProductDTO
import com.vestis.domain.products.model.ProductModel

fun List<ProductEntity>.toDomain() = this
    .map { it.toDomain() }

fun ProductEntity.toDomain() = ProductModel(
    id = this.id,
    title = this.title,
    price = this.price,
    category = this.category,
    imageUrl = this.imageUrl

)

fun List<ProductDTO>.toProductEntity() = this
    .map { it.toProductEntity() }

fun ProductDTO.toProductEntity() = ProductEntity(
    id = this.id,
    title = this.title,
    price = this.price,
    description = this.description,
    category = this.category,
    imageUrl = this.image
)