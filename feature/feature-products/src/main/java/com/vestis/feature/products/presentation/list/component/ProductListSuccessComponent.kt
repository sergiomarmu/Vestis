package com.vestis.feature.products.presentation.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.vestis.core.presentation.component.product.ProductCard
import com.vestis.domain.products.model.ProductModel
import com.vestis.feature.products.presentation.list.ProductListIntent
import com.vestis.feature.products.presentation.list.component.parameterprovider.ProductListParameterProvider
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ProductListSuccessComponent(
    products: PersistentList<ProductModel>,
    onIntent: (ProductListIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id },
            contentType = { "product" }
        ) { product ->
            ProductCard(
                title = product.title,
                price = product.price,
                category = product.category,
                isFavorite = product.isFavorite,
                imageUrl = product.imageUrl,
                onToggleFavorite = {
                    onIntent(
                        ProductListIntent.ToggleFavorite(
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
private fun ProductListSuccessComponentPreview(
    @PreviewParameter(provider = ProductListParameterProvider::class) products: List<ProductModel>
) {
    ProductListSuccessComponent(
        products = products.toPersistentList(),
        onIntent = {}
    )
}