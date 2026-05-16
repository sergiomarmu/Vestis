package com.vestis.data.favorite.respository

import app.cash.turbine.test
import com.vestis.data.favorite.local.datasource.FavoriteLocalDataSource
import com.vestis.data.favorite.repository.FavoriteRepositoryImpl
import com.vestis.domain.favorite.repository.FavoriteRepository
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

class FavoriteRepositoryImplTest {

    private lateinit var sut: FavoriteRepository

    @MockK
    private lateinit var favoriteLocalDataSource: FavoriteLocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = FavoriteRepositoryImpl(
            favoriteLocalDataSource = favoriteLocalDataSource
        )
    }

    @Test
    fun `GIVEN a source of IDs WHEN getFavoriteIdsFlows is executed THEN return a flow of sets`() =
        runTest {
            // Given
            val favoriteIds = listOf(1, 2, 3)
            every { favoriteLocalDataSource.observeAll() } returns flowOf(favoriteIds)

            // When
            sut.getFavoriteIdsFlows().test {
                val result = awaitItem()

                // Then
                assert(result.size == 3)
                assert(result.contains(1))
                awaitComplete()
            }

            verify(exactly = 1) {
                favoriteLocalDataSource.observeAll()
            }
        }

    @Test
    fun `GIVEN a non existing product WHEN toggleFavorite is executed THEN insert into local data source`() =
        runTest {
            // Given
            val productId = 1

            coEvery { favoriteLocalDataSource.exists(productId) } returns false
            coEvery { favoriteLocalDataSource.insert(productId) } returns Unit

            // When
            sut.toggleFavorite(productId)

            // Then
            coVerify(exactly = 0) {
                favoriteLocalDataSource.delete(any())
            }

            coVerify(exactly = 1) {
                favoriteLocalDataSource.exists(productId)
                favoriteLocalDataSource.insert(productId)
            }
        }

    @Test
    fun `GIVEN an existing product WHEN toggleFavorite is executed THEN delete from local data source`() =
        runTest {
            // Given
            val productId = 1
            coEvery { favoriteLocalDataSource.exists(productId) } returns true
            coEvery { favoriteLocalDataSource.delete(productId) } returns Unit

            // When
            sut.toggleFavorite(productId)

            // Then
            coVerify(exactly = 0) {
                favoriteLocalDataSource.insert(any())
            }

            coVerify(exactly = 1) {
                favoriteLocalDataSource.exists(productId)
                favoriteLocalDataSource.delete(productId)
            }
        }

    @Test
    fun `GIVEN a product WHEN isFavorite is executed THEN return data source existence`() =
        runTest {
            // Given
            val productId = 1
            coEvery { favoriteLocalDataSource.exists(productId) } returns true

            // When
            val result = sut.isFavorite(productId)

            // Then
            assert(result)

            coVerify(exactly = 1) {
                favoriteLocalDataSource.exists(productId)
            }
        }
}