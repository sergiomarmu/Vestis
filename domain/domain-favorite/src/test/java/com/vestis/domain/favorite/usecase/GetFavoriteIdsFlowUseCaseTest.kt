package com.vestis.domain.favorite.usecase

import app.cash.turbine.test
import com.vestis.domain.favorite.repository.FavoriteRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class GetFavoriteIdsFlowUseCaseTest {

    private lateinit var sut: GetFavoriteIdsFlowUseCase

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = GetFavoriteIdsFlowUseCase(
            favoriteRepository = favoriteRepository
        )
    }

    @Test
    fun `GIVEN empty favorites WHEN sut is called THEN return empty set`() = runTest {
        // Given
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
    fun `GIVEN favorite ids WHEN sut is called THEN return those ids`() = runTest {
        // Given
        val favoriteIds = setOf(1, 3, 5)

        every {
            favoriteRepository.getFavoriteIdsFlow()
        } returns flowOf(value = favoriteIds)

        // When
        sut.invoke().test {
            // Then
            val result = awaitItem()

            assert(result.size == 3)
            assert(result.contains(1))
            assert(result.contains(3))
            assert(!result.contains(2))

            awaitComplete()
        }
    }
}