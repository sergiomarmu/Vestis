@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.favorites

import app.cash.turbine.test
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.usecase.GetFavoriteProductsFlowUseCase
import com.vestis.feature.favorites.presentation.FavoriteListEffect
import com.vestis.feature.favorites.presentation.FavoriteListIntent
import com.vestis.feature.favorites.presentation.FavoriteListState
import com.vestis.feature.favorites.presentation.FavoriteListViewModel
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

class FavoriteListViewModelTest {

    private lateinit var sut: FavoriteListViewModel

    @MockK
    private lateinit var getFavoriteProductsFlowUseCase: GetFavoriteProductsFlowUseCase

    @MockK
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        sut = FavoriteListViewModel(
            getFavoriteProductsFlowUseCase = getFavoriteProductsFlowUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase
        )
    }

    @Test
    fun `GIVEN failure from use case WHEN init intent is executed THEN state should be error`() =
        runTest {
            // Given
            every {
                getFavoriteProductsFlowUseCase.invoke()
            } returns flow {
                throw Exception("Error fetching favorites")
            }

            sut.uiState.test {
                assert(awaitItem() == FavoriteListState.Idle)

                // When
                sut.handleIntent(FavoriteListIntent.Init)

                // Then
                assert(awaitItem() == FavoriteListState.Loading)

                val finalState = awaitItem()

                assert(finalState is FavoriteListState.Error)
                assert((finalState as FavoriteListState.Error).message == "Error fetching favorites")
            }
        }

    @Test
    fun `GIVEN empty list from use case WHEN init intent is executed THEN state should be empty`() =
        runTest {
            // Given
            every {
                getFavoriteProductsFlowUseCase.invoke()
            } returns flowOf(value = emptyList())

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
            every {
                getFavoriteProductsFlowUseCase.invoke()
            } returns flowOf(value = listOf(mockk<ProductModel>(relaxed = true)))

            sut.uiState.test {
                assert(awaitItem() == FavoriteListState.Idle)

                // When
                sut.handleIntent(FavoriteListIntent.Init)

                // Then
                assert(awaitItem() == FavoriteListState.Loading)

                val finalState = awaitItem()

                assert(finalState is FavoriteListState.Success)
                assert((finalState as FavoriteListState.Success).products.size == 1)
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
                val effect = awaitItem()

                assert(effect is FavoriteListEffect.ShowError)
                assert((effect as FavoriteListEffect.ShowError).message == errorMessage)
            }
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}