package com.vestis.feature.products.presentation.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vestis.core.presentation.ui.theme.Gray500
import com.vestis.core.presentation.ui.theme.Gray900
import com.vestis.core.presentation.ui.theme.Shapes
import com.vestis.core.presentation.ui.theme.Surface
import com.vestis.domain.products.model.ProductModel
import com.vestis.feature.products.presentation.list.component.parameterprovider.ProductListParameterProvider
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ProductListSuccessComponent(
    products: PersistentList<ProductModel>,
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
            contentType = { "product" }
        ) { product ->
            ProductCard(
                product = product
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            ProductCardImage(
                title = product.title,
                imageUrl = product.imageUrl
            )

            ProductCardInfo(
                title = product.title,
                category = product.category,
                price = product.price
            )
        }
    }
}

@Composable
private fun ProductCardImage(
    title: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
    ) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null
                )
            }
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun ProductCardInfo(
    title: String,
    category: String,
    price: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(all = 12.dp)
    ) {
        Text(
            text = category.uppercase(),
            fontSize = 12.sp,
            color = Gray500,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray900,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "$${String.format("%.2f", price)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
    }
}

@Preview
@Composable
fun ProductListSuccessComponentPreview(
    @PreviewParameter(provider = ProductListParameterProvider::class) products: List<ProductModel>
) {
    ProductListSuccessComponent(
        products = products.toPersistentList()
    )
}

@Preview
@Composable
fun ProductCardPreview(
    @PreviewParameter(provider = ProductListParameterProvider::class) products: List<ProductModel>
) {
    ProductCard(
        product = products.first()
    )
}

