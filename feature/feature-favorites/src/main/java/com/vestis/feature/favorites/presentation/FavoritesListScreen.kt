import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vestis.feature.favorites.presentation.FavoriteListIntent
import com.vestis.feature.favorites.presentation.FavoriteListState
import com.vestis.feature.favorites.presentation.FavoriteListViewModel
import com.vestis.feature.favorites.presentation.component.FavoriteListEmptyComponent
import com.vestis.feature.favorites.presentation.component.FavoriteListErrorComponent
import com.vestis.feature.favorites.presentation.component.FavoriteListLoadingComponent
import com.vestis.feature.favorites.presentation.component.FavoriteListSuccessComponent

@Composable
fun FavoritesListScreen(
    viewModel: FavoriteListViewModel = hiltViewModel<FavoriteListViewModel>(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleIntent(intent = FavoriteListIntent.Init)
    }

    ProductListContent(
        state = state,
        onIntent = { viewModel.handleIntent(it) },
        modifier = modifier
    )
}

@Composable
private fun ProductListContent(
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

        is FavoriteListState.Error -> FavoriteListErrorComponent(
            message = state.message,
            modifier = modifier,
        )

        is FavoriteListState.Success -> FavoriteListSuccessComponent(
            products = state.products,
            onIntent = { onIntent(it) },
            modifier = modifier,
        )
    }
}