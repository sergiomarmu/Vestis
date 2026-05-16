package com.vestis.feature.favorites.presentation

import androidx.compose.runtime.Stable
import com.vestis.core.presentation.base.UiEffect
import com.vestis.core.presentation.base.UiIntent
import com.vestis.core.presentation.base.UiState
import com.vestis.domain.products.model.ProductModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList

sealed interface FavoriteListState : UiState {

    data object Idle : FavoriteListState

    data object Loading : FavoriteListState

    @Stable
    data class Success(
        val products: PersistentList<ProductModel>
    ) : FavoriteListState

    data object Empty : FavoriteListState

    data class Error(
        val message: String
    ) : FavoriteListState
}

sealed interface FavoriteListIntent : UiIntent {
    data object Init : FavoriteListIntent

    data class ToggleFavorite(
        val productId: Int
    ) : FavoriteListIntent
}


sealed interface FavoriteListEffect : UiEffect {

    data class ShowError(
        val message: String
    ) : FavoriteListEffect
}