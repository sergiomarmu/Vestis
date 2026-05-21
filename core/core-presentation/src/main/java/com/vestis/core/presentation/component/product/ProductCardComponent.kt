package com.vestis.core.presentation.component.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vestis.core.presentation.R
import com.vestis.core.presentation.component.product.parameterprovider.ProductCardPreviewData
import com.vestis.core.presentation.component.product.parameterprovider.ProductListParameterProvider
import com.vestis.core.presentation.utils.formatter.toFormattedPrice
import com.vestis.core.presentation.ui.theme.Gray400
import com.vestis.core.presentation.ui.theme.Gray500
import com.vestis.core.presentation.ui.theme.Gray900
import com.vestis.core.presentation.ui.theme.Red500
import com.vestis.core.presentation.ui.theme.Shapes
import com.vestis.core.presentation.ui.theme.Surface

@Composable
fun ProductCardComponent(
    title: String,
    price: Float,
    category: String,
    isFavorite: Boolean,
    imageUrl: String,
    onToggleFavorite: () -> Unit,
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
                title = title,
                imageUrl = imageUrl,
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite
            )

            ProductCardInfo(
                title = title,
                category = category,
                price = price
            )
        }
    }
}

@Composable
private fun ProductCardImage(
    title: String,
    imageUrl: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
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
                    contentScale = ContentScale.Fit
                )
            }
        }

        IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Surface,
                contentColor = if (isFavorite)
                    Red500
                else
                    Gray400,
            ),
        ) {
            Icon(
                imageVector = if (isFavorite)
                    Icons.Filled.Favorite
                else
                    Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite)
                    stringResource(id = R.string.core_remove_content_description)
                else
                    stringResource(id = R.string.core_add_content_description),
                modifier = Modifier.size(32.dp),
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
            style = MaterialTheme.typography.bodySmall,
            color = Gray500,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Gray900,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = price.toFormattedPrice(),
            style = MaterialTheme.typography.labelLarge,
            color = Gray900
        )
    }
}

@Preview
@Composable
fun ProductCardComponentPreview(
    @PreviewParameter(provider = ProductListParameterProvider::class) data: ProductCardPreviewData
) {
    ProductCardComponent(
        title = data.title,
        price = data.price,
        category = data.category,
        isFavorite = data.isFavorite,
        imageUrl = data.imageUrl,
        onToggleFavorite = {}
    )
}

