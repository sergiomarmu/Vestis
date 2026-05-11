package com.vestis.data.products.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "title") val title: String,
    @SerialName(value = "price") val price: Float,
    @SerialName(value = "description") val description: String,
    @SerialName(value = "category") val category: String,
    @SerialName(value = "image") val image: String
)