package com.vestis.domain.profile.usecase

import com.vestis.domain.favorite.repository.FavoriteRepository
import com.vestis.domain.favorite.usecase.ToggleFavoriteUseCase
import com.vestis.domain.profile.repository.ProfileRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProfileUseCaseTest {

    private lateinit var sut: GetProfileUseCase

    @MockK
    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = GetProfileUseCase(
            profileRepository = profileRepository
        )
    }

    @Test
    fun `WHEN sut is called THEN call repository toggleFavorite function`() = runTest {
        // Given
        coEvery { profileRepository.getProfile() } returns mockk()

        // When
        sut.invoke()

        // Then
        coVerify(exactly = 1) { profileRepository.getProfile() }
    }
}