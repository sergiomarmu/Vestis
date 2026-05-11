package com.vestis.data.products.network.api

import com.vestis.data.products.network.dto.ProductDTO
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApiService {
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductDTO>>
}