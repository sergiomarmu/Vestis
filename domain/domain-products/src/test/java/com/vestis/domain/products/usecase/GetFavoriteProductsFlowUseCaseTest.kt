package com.vestis.domain.products.usecase

import app.cash.turbine.test
import com.vestis.domain.favorite.repository.FavoriteRepository
import com.vestis.domain.products.model.ProductModel
import com.vestis.domain.products.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetFavoriteProductsFlowUseCaseTest {

    private lateinit var sut: GetFavoriteProductsFlowUseCase

    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = GetFavoriteProductsFlowUseCase(
            productRepository = productRepository,
            favoriteRepository = favoriteRepository
        )
    }

    @Test
    fun `GIVEN empty products and favorites WHEN sut is called THEN return empty list`() = runTest {
        // Given
        every {
            productRepository.getProductsFlow(any())
        } returns flowOf(value = emptyList())

        every {
            favoriteRepository.getFavoriteIdsFlow()
        } returns flowOf(value = emptySet())

        // When
        sut.invoke().test {
            // Then
            val result = awaitItem()

            assert(result.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN products and some favorite IDs WHEN sut is called THEN return only favorites`() =
        runTest {
            // Given
            val products = listOf(
                ProductModel(
                    id = 1,
                    title = "Fav",
                    price = 10f,
                    category = "",
                    imageUrl = "",
                    isFavorite = false
                ),
                ProductModel(
                    id = 2,
                    title = "No Fav",
                    price = 10f,
                    category = "",
                    imageUrl = "",
                    isFavorite = false
                )
            )

            val favoriteIds = setOf(1)

            every {
                productRepository.getProductsFlow(any())
            } returns flowOf(value = products)

            every {
                favoriteRepository.getFavoriteIdsFlow()
            } returns flowOf(value = favoriteIds)

            // When
            sut.invoke().test {
                val result = awaitItem()

                // Then
                assert(result.size == 1)
                assert(result.first().id == 1)
                assert(result.first().isFavorite)
                awaitComplete()
            }
        }

    @Test
    fun `GIVEN products AND empty favorites WHEN sut is called THEN return empty list`() = runTest {
        // Given
        val products = listOf(
            ProductModel(
                id = 1,
                title = "Shirt",
                price = 10f,
                category = "",
                imageUrl = "",
                isFavorite = false
            )
        )

        every {
            productRepository.getProductsFlow(any())
        } returns flowOf(value = products)

        every {
            favoriteRepository.getFavoriteIdsFlow()
        } returns flowOf(value = emptySet())

        // When
        sut.invoke().test {
            // Then
            val result = awaitItem()

            assert(result.isEmpty())
            awaitComplete()
        }
    }
}