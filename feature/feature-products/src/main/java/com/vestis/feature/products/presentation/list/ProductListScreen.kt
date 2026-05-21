package com.vestis.feature.products.presentation.list

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vestis.feature.products.presentation.list.component.ProductListEmptyComponent
import com.vestis.feature.products.presentation.list.component.ProductListErrorComponent
import com.vestis.feature.products.presentation.list.component.ProductListLoadingComponent
import com.vestis.feature.products.presentation.list.component.ProductListSuccessComponent
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel<ProductListViewModel>(),
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleIntent(intent = ProductListIntent.Init)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ProductListEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState
                            .showSnackbar(
                                message = effect.text.asString(context),
                                duration = SnackbarDuration.Short
                            )
                    }
                }
            }
        }
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
            message = state.text.asString(),
            onIntent = { onIntent(it) },
            modifier = modifier,
        )

        is ProductListState.Success -> ProductListSuccessComponent(
            products = state.products,
            onIntent = { onIntent(it) },
            modifier = modifier,
        )
    }
}