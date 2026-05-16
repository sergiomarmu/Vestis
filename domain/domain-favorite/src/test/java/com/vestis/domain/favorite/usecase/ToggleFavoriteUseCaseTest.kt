package com.vestis.domain.favorite.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private lateinit var sut: ToggleFavoriteUseCase

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ToggleFavoriteUseCase(
            favoriteRepository = favoriteRepository
        )
    }

    @Test
    fun `WHEN sut is called THEN call repository toggleFavorite function`() = runTest {
        // Given
        val productId = 1

        coEvery { favoriteRepository.toggleFavorite(productId) } returns Unit

        // When
        sut.invoke(productId)

        // Then
        coVerify(exactly = 1) { favoriteRepository.toggleFavorite(productId) }
    }
}