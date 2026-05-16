package com.vestis.feature.products.presentation.list

import androidx.compose.runtime.Stable
import com.vestis.core.presentation.base.UiEffect
import com.vestis.core.presentation.base.UiIntent
import com.vestis.core.presentation.base.UiState
import com.vestis.domain.products.model.ProductModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList

sealed interface ProductListState : UiState {

    data object Idle : ProductListState

    data object Loading : ProductListState

    @Stable
    data class Success(
        val products: PersistentList<ProductModel>
    ) : ProductListState

    data object Empty : ProductListState

    data class Error(
        val message: String
    ) : ProductListState
}

sealed interface ProductListIntent : UiIntent {
    data object Init : ProductListIntent

    data object Retry: ProductListIntent

    data class ToggleFavorite(
        val productId: Int
    ) : ProductListIntent
}

sealed interface ProductListEffect : UiEffect {
    data class ShowError(
        val message: String
    ) : ProductListEffect
}