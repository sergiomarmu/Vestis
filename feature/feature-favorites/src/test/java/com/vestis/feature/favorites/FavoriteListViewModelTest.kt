@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.favorites

import app.cash.turbine.test
import com.vestis.domain.favorite.usecase.GetFavoriteIdsFlowUseCase
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.usecase.GetProductsFlowUseCase
import com.vestis.feature.favorites.presentation.FavoriteListEffect
import com.vestis.feature.favorites.presentation.FavoriteListIntent
import com.vestis.feature.favorites.presentation.FavoriteListState
import com.vestis.feature.favorites.presentation.FavoriteListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

class FavoriteListViewModelTest {

    private lateinit var sut: FavoriteListViewModel

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

        sut = FavoriteListViewModel(
            getProductsFlowUseCase = getProductsFlowUseCase,
            getFavoriteIdsFlowUseCase = getFavoriteIdsFlowUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase
        )
    }

    @Test
    fun `GIVEN failure from use case WHEN init intent is executed THEN state should be empty`() =
        runTest {
            // Given
            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flow {
                throw Exception("Error fetching favorites")
            }

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = emptySet())

            sut.uiState.test {
                assert(awaitItem() == FavoriteListState.Idle)

                // When
                sut.handleIntent(FavoriteListIntent.Init)

                // Then
                assert(awaitItem() == FavoriteListState.Loading)
                assert(awaitItem() is FavoriteListState.Empty)
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
                assert(awaitItem() == FavoriteListState.Idle)

                // When
                sut.handleIntent(FavoriteListIntent.Init)

                // Then
                assert(awaitItem() == FavoriteListState.Loading)
                assert(awaitItem() == FavoriteListState.Empty)
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
                    isFavorite = true
                ),
                ProductModel(
                    id = 2,
                    title = "Test Product2",
                    price = 19.99f,
                    category = "Women's clothes",
                    imageUrl = "url",
                    isFavorite = false
                )
            )

            every {
                getProductsFlowUseCase.invoke(forceNetwork = false)
            } returns flowOf(value = products)

            every {
                getFavoriteIdsFlowUseCase.invoke()
            } returns flowOf(value = setOf(1))

            sut.uiState.test {
                assert(awaitItem() == FavoriteListState.Idle)

                // When
                sut.handleIntent(FavoriteListIntent.Init)

                // Then
                assert(awaitItem() == FavoriteListState.Loading)

                val successState = awaitItem()
                assert(successState is FavoriteListState.Success)

                val processedProducts = (successState as FavoriteListState.Success)
                assert(processedProducts.products.size == 1)
                assert(processedProducts.products[0].isFavorite)
            }
        }

    @Test
    fun `GIVEN a product ID WHEN toggle favorite intent is executed THEN call use case`() =
        runTest {
            // Given
            val productId = 1

            coEvery { toggleFavoriteUseCase.invoke(productId) } returns Unit

            // When
            sut.handleIntent(FavoriteListIntent.ToggleFavorite(productId))

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
                sut.handleIntent(FavoriteListIntent.ToggleFavorite(productId))

                // Then
                assert(awaitItem() is FavoriteListEffect.ShowError)
            }
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}