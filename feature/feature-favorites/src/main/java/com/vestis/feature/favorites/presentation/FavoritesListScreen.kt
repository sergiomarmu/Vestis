package com.vestis.feature.favorites.presentation

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
import com.vestis.feature.favorites.presentation.component.FavoriteListEmptyComponent
import com.vestis.feature.favorites.presentation.component.FavoriteListLoadingComponent
import com.vestis.feature.favorites.presentation.component.FavoriteListSuccessComponent
import kotlinx.coroutines.launch

@Composable
fun FavoritesListScreen(
    viewModel: FavoriteListViewModel = hiltViewModel<FavoriteListViewModel>(),
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleIntent(intent = FavoriteListIntent.Init)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is FavoriteListEffect.ShowError -> {
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

    FavoriteListContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier
    )
}

@Composable
private fun FavoriteListContent(
    state: FavoriteListState,
    onIntent: (FavoriteListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        FavoriteListState.Idle,
        FavoriteListState.Loading -> FavoriteListLoadingComponent(
            modifier = modifier
        )

        FavoriteListState.Empty -> FavoriteListEmptyComponent(
            modifier = modifier
        )

        is FavoriteListState.Success -> FavoriteListSuccessComponent(
            products = state.products,
            onIntent = { onIntent(it) },
            modifier = modifier,
        )
    }
}