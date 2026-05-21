@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.products

import app.cash.turbine.test
import com.vestis.core.domain.DomainException
import com.vestis.domain.favorite.usecase.GetFavoriteIdsFlowUseCase
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.usecase.GetProductsFlowUseCase
import com.vestis.feature.products.presentation.list.ProductListEffect
import com.vestis.feature.products.presentation.list.ProductListIntent
import com.vestis.feature.products.presentation.list.ProductListState
import com.vestis.feature.products.presentation.list.ProductListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    @MockK
    private lateinit var getFavoriteIdsFlowUseCase: GetFavoriteIdsFlowUseCase

    @MockK
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        sut = ProductListViewModel(
            getProductsFlowUseCase = getProductsFlowUseCase,
            getFavoriteIdsFlowUseCase = getFavoriteIdsFlowUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase
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

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = emptySet())

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

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = emptySet())

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
            val products = listOf(
                ProductModel(
                    id = 1,
                    title = "Test Product",
                    price = 19.99f,
                    category = "Men's clothes",
                    imageUrl = "url",
                    isFavorite = false
                ),
                ProductModel(
                    id = 2,
                    title = "Test Product2",
                    price = 19.99f,
                    category = "Women's clothes",
                    imageUrl = "url",
                    isFavorite = true
                )
            )

            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flowOf(value = products)

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = setOf(2))

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                // When
                sut.handleIntent(ProductListIntent.Init)

                // Then
                assert(awaitItem() == ProductListState.Loading)

                val successState = awaitItem()
                assert(successState is ProductListState.Success)

                val processedProducts = (successState as ProductListState.Success)
                assert(processedProducts.products.size == 2)
                assert(processedProducts.products[1].isFavorite)
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

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = emptySet())

            sut.uiState.test {
                assert(awaitItem() == ProductListState.Idle)

                // First failure try
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

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = emptySet())

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

    @Test
    fun `GIVEN a product ID WHEN toggle favorite intent is executed THEN call use case`() =
        runTest {
            // Given
            val productId = 1

            coEvery { toggleFavoriteUseCase.invoke(productId) } returns Unit

            // When
            sut.handleIntent(ProductListIntent.ToggleFavorite(productId))

            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { toggleFavoriteUseCase.invoke(productId) }
        }

    @Test
    fun `GIVEN toggle favorite failure WHEN toggle favorite intent is executed THEN send error effect`() =
        runTest {
            // Given
            val productId = 1
            val errorMessage = "Toggle failed"

            coEvery { toggleFavoriteUseCase.invoke(productId) } throws Exception(errorMessage)

            sut.uiEffect.test {
                // When
                sut.handleIntent(ProductListIntent.ToggleFavorite(productId))

                // Then
                val effect = awaitItem()

                assert(effect is ProductListEffect.ShowError)
            }
        }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}