package com.vestis.feature.favorites.presentation

import androidx.lifecycle.viewModelScope
import com.vestis.core.presentation.base.BaseMviViewModel
import com.vestis.core.presentation.utils.text.asUiText
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.products.usecase.GetFavoriteProductsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val getFavoriteProductsFlowUseCase: GetFavoriteProductsFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : BaseMviViewModel<FavoriteListState, FavoriteListIntent, FavoriteListEffect>(
    initialState = FavoriteListState.Idle
) {
    override fun handleIntent(
        intent: FavoriteListIntent
    ) {
        when (intent) {
            FavoriteListIntent.Init -> processInit()
            is FavoriteListIntent.ToggleFavorite -> processToggleFavorite(
                intent = intent
            )
        }
    }

    private fun processInit() {
        if (uiState.value is FavoriteListState.Success) return

        updateState { FavoriteListState.Loading }

        getFavoriteProductsFlowUseCase.invoke()
            .onEach {
                if (it.isEmpty()) {
                    updateState { FavoriteListState.Empty }
                } else {
                    updateState {
                        FavoriteListState.Success(
                            products = it.toPersistentList()
                        )
                    }

                }
            }.catch {
                updateState { FavoriteListState.Empty }
            }.launchIn(viewModelScope)
    }

    private fun processToggleFavorite(
        intent: FavoriteListIntent.ToggleFavorite
    ) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase.invoke(
                    productId = intent.productId
                )
            } catch (t: Throwable) {
                sendEffect(
                    FavoriteListEffect.ShowError(
                        text = t.asUiText()
                    )
                )
            }
        }
    }
}