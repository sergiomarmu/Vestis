package com.vestis.data.products.repository

import app.cash.turbine.test
import com.vestis.core.common.either.Either
import com.vestis.core.data.exception.DataException
import com.vestis.core.domain.DomainException
import com.vestis.data.products.local.datasource.ProductLocalDataSource
import com.vestis.data.products.network.datasources.ProductNetworkDataSource
import com.vestis.data.products.network.dto.ProductDTO
import com.vestis.domain.products.repository.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var sut: ProductRepository

    @MockK
    private lateinit var productNetworkDataSource: ProductNetworkDataSource

    @MockK
    private lateinit var productLocalDataSource: ProductLocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ProductRepositoryImpl(
            productNetworkDataSource = productNetworkDataSource,
            productLocalDataSource = productLocalDataSource
        )
    }

    @Test
    fun `GIVEN an empty DB WHEN getProductsFlow is executed THEN fetch from network and save them to database`() =
        runTest {
            // Given
            val networkProducts = listOf(
                ProductDTO(
                    id = 1,
                    title = "Shirt",
                    price = 1f,
                    description = "description",
                    category = "clothing",
                    image = "image"
                ),
                ProductDTO(
                    id = 2,
                    title = "TV",
                    price = 1f,
                    description = "description",
                    category = "electronics",
                    image = "image"
                )
            )

            coEvery { productLocalDataSource.getCount() } returns 0
            coEvery { productNetworkDataSource.getProducts() } returns Either.Right(value = networkProducts)
            coEvery { productLocalDataSource.upsertAll(products = any()) } returns Unit
            every { productLocalDataSource.observeAll() } returns flowOf(value = emptyList())

            // When
            sut.getProductsFlow(forceNetwork = false).test {
                awaitItem()
                awaitComplete()
            }

            // Then
            coVerify(exactly = 1) {
                productNetworkDataSource.getProducts()
                productLocalDataSource.upsertAll(products = any())
            }

            verify(exactly = 1) {
                productLocalDataSource.observeAll()
            }
        }

    @Test
    fun `GIVEN a DB with data WHEN getProductsFlow without forceNetwork is executed THEN do not fetch from network`() =
        runTest {
            // Given
            coEvery { productLocalDataSource.getCount() } returns 10
            every { productLocalDataSource.observeAll() } returns flowOf(value = emptyList())

            // When
            sut.getProductsFlow(forceNetwork = false).test {
                awaitItem()
                awaitComplete()
            }

            // Then
            coVerify(exactly = 0) { productNetworkDataSource.getProducts() }

            verify(exactly = 1) {
                productLocalDataSource.observeAll()
            }
        }

    @Test
    fun `GIVEN a DB with data AND network fails WHEN getProductsFlow with forceNetwork is executed THEN ignore error and show cache data`() =
        runTest {
            // Given
            coEvery { productLocalDataSource.getCount() } returns 5
            coEvery { productNetworkDataSource.getProducts() } returns Either.Left(value = DataException.Network.Unreachable())
            every { productLocalDataSource.observeAll() } returns flowOf(value = emptyList())

            // When
            sut.getProductsFlow(forceNetwork = true).test {
                awaitItem()
                awaitComplete()
            }

            // Then
            coVerify(exactly = 0) {
                productLocalDataSource.upsertAll(products = any())
            }

            coVerify(exactly = 1) {
                productLocalDataSource.getCount()
                productNetworkDataSource.getProducts()
            }

            verify(exactly = 1) {
                productLocalDataSource.observeAll()
            }
        }

    @Test
    fun `GIVEN an empty DB AND network fails WHEN getProductsFlow is executed THEN throw exception`() =
        runTest {
            // Given
            coEvery { productLocalDataSource.getCount() } returns 0
            coEvery { productNetworkDataSource.getProducts() } returns Either.Left(value = DataException.Network.Unreachable())
            every { productLocalDataSource.observeAll() } returns flowOf(value = emptyList())

            // When | Then
            sut.getProductsFlow(forceNetwork = false).test {
                val exception = awaitError()

                assert(exception is DomainException.Network.NoConnection)
            }

            verify(exactly = 0) {
                productLocalDataSource.observeAll()
            }

            coVerify(exactly = 1) {
                productLocalDataSource.getCount()
                productNetworkDataSource.getProducts()
            }
        }
}