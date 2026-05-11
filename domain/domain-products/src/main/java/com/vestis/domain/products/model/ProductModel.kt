package com.vestis.domain.products.model

data class ProductModel(
    val id: Int,
    val title: String,
    val price: Float,
    val category: String,
    val imageUrl: String
)