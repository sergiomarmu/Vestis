package com.vestis.feature.favorites.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.vestis.core.presentation.component.product.ProductCardComponent
import com.vestis.core.presentation.component.product.parameterprovider.ProductListParameterProvider
import com.vestis.domain.products.model.ProductModel
import com.vestis.feature.favorites.presentation.FavoriteListIntent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FavoriteListSuccessComponent(
    products: PersistentList<ProductModel>,
    onIntent: (FavoriteListIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id },
            contentType = { "favorite" }
        ) { product ->
            ProductCardComponent(
                title = product.title,
                price = product.price,
                category = product.category,
                isFavorite = product.isFavorite,
                imageUrl = product.imageUrl,
                onToggleFavorite = {
                    onIntent(
                        FavoriteListIntent.ToggleFavorite(
                            productId = product.id
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun FavoriteListSuccessComponentPreview(
    @PreviewParameter(provider = ProductListParameterProvider::class) products: List<ProductModel>
) {
    FavoriteListSuccessComponent(
        products = products.toPersistentList(),
        onIntent = {}
    )
}