@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.products.presentation.list

import androidx.lifecycle.viewModelScope
import com.vestis.core.domain.DomainException
import com.vestis.core.presentation.base.BaseMviViewModel
import com.vestis.core.presentation.mapper.toUiMessage
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.products.usecase.GetProductsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsFlowUseCase: GetProductsFlowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : BaseMviViewModel<ProductListState, ProductListIntent, ProductListEffect>(
    initialState = ProductListState.Idle
) {
    private val refresh = MutableSharedFlow<Boolean>(replay = 0)

    override fun handleIntent(
        intent: ProductListIntent
    ) {
        when (intent) {
            ProductListIntent.Init -> processInit()
            ProductListIntent.Retry -> processRetry()
            is ProductListIntent.ToggleFavorite -> processToggleFavorite(
                intent = intent
            )
        }
    }

    private fun processInit() {
        if (uiState.value is ProductListState.Success) return

        updateState { ProductListState.Loading }

        refresh
            .onStart { emit(false) }
            .flatMapLatest { forceNetwork ->
                getProductsFlowUseCase.invoke(
                    forceNetwork = forceNetwork
                ).onEach {
                    if (it.isEmpty()) {
                        updateState { ProductListState.Empty }
                    } else {
                        updateState {
                            ProductListState.Success(
                                products = it.toPersistentList()
                            )
                        }

                    }
                }.catch {
                    val message = if (it is DomainException) {
                        it.toUiMessage()
                    } else {
                        it.message
                    }

                    updateState {
                        ProductListState.Error(
                            message = message
                                ?: "Unknown error occurred"
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun processRetry() {
        updateState { ProductListState.Loading }

        viewModelScope.launch {
            refresh.emit(true)
        }
    }

    private fun processToggleFavorite(
        intent: ProductListIntent.ToggleFavorite
    ) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase.invoke(
                    productId = intent.productId
                )
            } catch (t: Throwable) {
                sendEffect(
                    ProductListEffect.ShowError(
                        message = t.message ?: "Unknown error"
                    )
                )
            }
        }
    }
}