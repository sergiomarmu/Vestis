package com.vestis.feature.products.presentation.list

import com.vestis.core.presentation.base.UiEffect
import com.vestis.core.presentation.base.UiIntent
import com.vestis.core.presentation.base.UiState
import com.vestis.core.presentation.utils.text.UiText
import com.vestis.domain.products.model.ProductModel
import kotlinx.collections.immutable.PersistentList

sealed interface ProductListState : UiState {

    data object Idle : ProductListState

    data object Loading : ProductListState

    data class Success(
        val products: PersistentList<ProductModel>
    ) : ProductListState

    data object Empty : ProductListState

    data class Error(
        val text: UiText
    ) : ProductListState
}

sealed interface ProductListIntent : UiIntent {
    data object Init : ProductListIntent

    data object Retry : ProductListIntent

    data class ToggleFavorite(
        val productId: Int
    ) : ProductListIntent
}

sealed interface ProductListEffect : UiEffect {
    data class ShowError(
        val text: UiText
    ) : ProductListEffect
}