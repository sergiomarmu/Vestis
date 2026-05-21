package com.vestis.domain.products.usecase

import app.cash.turbine.test
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = GetProductsFlowUseCase(
            productRepository = productRepository
        )
    }

    @Test
    fun `GIVEN empty products WHEN sut is called THEN return empty list`() = runTest {
        // Given
        every {
            productRepository.getProductsFlow(any())
        } returns flowOf(value = emptyList())

        // When
        sut.invoke(forceNetwork = false).test {
            // Then
            val result = awaitItem()

            assert(result.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN products WHEN sut is called THEN return products`() =
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

            every {
                productRepository.getProductsFlow(forceNetwork = false)
            } returns flowOf(value = products)

            // When
            sut.invoke(forceNetwork = false).test {
                val result = awaitItem()

                // Then
                assert(result.size == 2)

                awaitComplete()
            }
        }
}