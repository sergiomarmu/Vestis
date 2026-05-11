package com.vestis.feature.products.presentation.list

import androidx.lifecycle.viewModelScope
import com.vestis.core.presentation.base.BaseMviViewModel
import com.vestis.domain.products.usecase.GetProductsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsFlowUseCase: GetProductsFlowUseCase
) : BaseMviViewModel<ProductListState, ProductListIntent, ProductListEffect>(
    initialState = ProductListState.Idle
) {
    var pepe = true
    override fun handleIntent(
        intent: ProductListIntent
    ) {
        when (intent) {
            ProductListIntent.Init -> processInit()
            ProductListIntent.Retry -> processRetry()
        }
    }

    private fun processInit() {
        loadItems(forceNetwork = false)
    }

    private fun processRetry() {
        loadItems(forceNetwork = true)
    }

    private var loadItemsJob: Job? = null
    private fun loadItems(
        forceNetwork: Boolean
    ) {
        updateState { ProductListState.Loading }

        loadItemsJob?.cancel()

        loadItemsJob = getProductsFlowUseCase.invoke(
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
            updateState {
                ProductListState.Error(
                    message = it.message
                        ?: "Unknown error occurred"
                )
            }
        }.launchIn(viewModelScope)
    }
}