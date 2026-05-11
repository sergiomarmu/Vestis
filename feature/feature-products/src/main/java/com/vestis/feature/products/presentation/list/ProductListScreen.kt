package com.vestis.feature.products.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vestis.feature.products.presentation.list.component.ProductListEmptyComponent
import com.vestis.feature.products.presentation.list.component.ProductListErrorComponent
import com.vestis.feature.products.presentation.list.component.ProductListLoadingComponent
import com.vestis.feature.products.presentation.list.component.ProductListSuccessComponent

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel<ProductListViewModel>(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleIntent(intent = ProductListIntent.Init)
    }

    ProductListContent(
        state = state,
        onIntent = { viewModel.handleIntent(it) },
        modifier = modifier
    )
}

@Composable
private fun ProductListContent(
    state: ProductListState,
    onIntent: (ProductListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ProductListState.Idle,
        ProductListState.Loading -> ProductListLoadingComponent(
            modifier = modifier
        )

        ProductListState.Empty -> ProductListEmptyComponent(
            onIntent = { onIntent(it) },
            modifier = modifier
        )

        is ProductListState.Error -> ProductListErrorComponent(
            message = state.message,
            onIntent = { onIntent(it) },
            modifier = modifier,
        )

        is ProductListState.Success -> ProductListSuccessComponent(
            products = state.products,
            modifier = modifier,
        )
    }
}