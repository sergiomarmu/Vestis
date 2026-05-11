@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.products

import app.cash.turbine.test
import com.vestis.core.domain.DomainException
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.usecase.GetProductsFlowUseCase
import com.vestis.feature.products.presentation.list.ProductListIntent
import com.vestis.feature.products.presentation.list.ProductListState
import com.vestis.feature.products.presentation.list.ProductListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductListViewModelTest {

    private lateinit var sut: ProductListViewModel

    @MockK
    private lateinit var getProductsFlowUseCase: GetProductsFlowUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(testDispatcher)

        sut = ProductListViewModel(
            getProductsFlowUseCase = getProductsFlowUseCase
        )
    }

    @Test
    fun `GIVEN failure from use case WHEN init intent is executed THEN state should be error`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flow {
                throw DomainException.Network.NoConnection()
            }

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                // When
                sut.handleIntent(ProductListIntent.Init)

                // Then
                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Error)
            }
        }

    @Test
    fun `GIVEN empty list from use case WHEN init intent is executed THEN state should be empty`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flowOf(value = emptyList())

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                // When
                sut.handleIntent(ProductListIntent.Init)

                // Then
                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() == ProductListState.Empty)
            }
        }

    @Test
    fun `GIVEN success from use case WHEN init intent is executed THEN state should be success`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flowOf(value = listOf(mockk<ProductModel>(relaxed = true)))

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                // When
                sut.handleIntent(ProductListIntent.Init)

                // Then
                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Success)
            }
        }

    @Test
    fun `GIVEN initial failure WHEN retry intent is executed THEN state should be success`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flow { throw DomainException.Network.NoConnection() }

            every {
                getProductsFlowUseCase.invoke(forceNetwork = true)
            } returns flowOf(value = listOf(mockk<ProductModel>(relaxed = true)))

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                sut.handleIntent(ProductListIntent.Init)

                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Error)

                // When
                sut.handleIntent(ProductListIntent.Retry)

                // Then
                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Success)
            }
        }

    @Test
    fun `GIVEN initial failure WHEN retry intent is executed THEN state should be error`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flow { throw DomainException.Network.NoConnection() }

            every {
                getProductsFlowUseCase.invoke(forceNetwork = true)
            } returns flow { throw DomainException.Network.NoConnection() }

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                sut.handleIntent(ProductListIntent.Init)

                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Error)

                // When
                sut.handleIntent(ProductListIntent.Retry)

                // Then
                assert(awaitItem() == ProductListState.Loading)
                assert(awaitItem() is ProductListState.Error)
            }
        }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}