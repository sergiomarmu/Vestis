package com.vestis.feature.products.presentation.list.component.parameterprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.vestis.domain.products.model.ProductModel

class ProductListParameterProvider : PreviewParameterProvider<List<ProductModel>> {
    override val values = sequenceOf(
        listOf(
            ProductModel(
                id = 1,
                title = "Mens Casual Premium Slim Fit T-Shirts",
                price = 22.3f,
                category = "men's clothing",
                imageUrl = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png"
            ),
            ProductModel(
                id = 2,
                title = "Mens Cotton Jacket",
                price = 55.9f,
                category = "men's clothing",
                imageUrl = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
                isFavorite = true
            ),
            ProductModel(
                id = 3,
                title = "BIYLACLESEN Women's 3-in-1 Snowboard Jacket Winter Coats",
                price = 56.99f,
                category = "women's clothing",
                imageUrl = "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_t.png"
            ))
    )
}