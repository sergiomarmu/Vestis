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

class GetProductsFlowUseCaseTest {

    private lateinit var sut: GetProductsFlowUseCase

    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = GetProductsFlowUseCase(
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
        sut.invoke(forceNetwork = false).test {
            // Then
            val result = awaitItem()

            assert(result.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN products and favorite IDs WHEN sut is called THEN return products with correct favoreites`() =
        runTest {
            // Given
            val products = listOf(
                ProductModel(
                    id = 1,
                    title = "Shirt1",
                    price = 10f,
                    category = "",
                    imageUrl = "",
                    isFavorite = false
                ),
                ProductModel(
                    id = 2,
                    title = "Shirt2",
                    price = 10f,
                    category = "",
                    imageUrl = "",
                    isFavorite = false
                )
            )

            val favoriteIds = setOf(1)

            every {
                productRepository.getProductsFlow(forceNetwork = false)
            } returns flowOf(value = products)

            every {
                favoriteRepository.getFavoriteIdsFlow()
            } returns flowOf(value = favoriteIds)

            // When
            sut.invoke(forceNetwork = false).test {
                val result = awaitItem()

                // Then
                assert(result.size == 2)

                val product1 = result.find { it.id == 1 }
                assert(product1?.isFavorite == true)

                val product2 = result.find { it.id == 2 }
                assert(product2?.isFavorite == false)

                awaitComplete()
            }
        }

    @Test
    fun `GIVEN empty favorites WHEN sut is called THEN all products should have isFavorite as false`() =
        runTest {
            // Given
            val products = listOf(
                ProductModel(
                    id = 1,
                    title = "Shirt1",
                    price = 10f,
                    category = "",
                    imageUrl = "",
                    isFavorite = false
                )
            )

            val favoriteIds = emptySet<Int>()

            every {
                productRepository.getProductsFlow(forceNetwork = false)
            } returns flowOf(value = products)

            every {
                favoriteRepository.getFavoriteIdsFlow()
            } returns flowOf(value = favoriteIds)

            // When
            sut.invoke(forceNetwork = false).test {
                val result = awaitItem()

                // Then
                assert(!result.first().isFavorite)
                awaitComplete()
            }
        }
}