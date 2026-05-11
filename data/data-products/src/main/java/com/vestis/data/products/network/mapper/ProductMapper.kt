package com.vestis.data.products.network.mapper

import com.vestis.data.products.network.dto.ProductDTO
import com.vestis.domain.products.model.ProductModel

fun List<ProductDTO>.toDomain() = this
    .map { it.toDomain() }

fun ProductDTO.toDomain() = ProductModel(
    id = this.id,
    title = this.title,
    price = this.price,
    category = this.category,
    imageUrl = this.image
)