@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.profile

import app.cash.turbine.test
import com.vestis.core.common.either.Either
import com.vestis.domain.favorite.usecase.GetFavoriteCountFlowUseCase
import com.vestis.domain.profile.usecase.GetProfileUseCase
import com.vestis.feature.profile.presentation.profile.ProfileIntent
import com.vestis.feature.profile.presentation.profile.ProfileState
import com.vestis.feature.profile.presentation.profile.ProfileViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest {

    private lateinit var sut: ProfileViewModel

    @MockK
    private lateinit var getProfileUseCase: GetProfileUseCase

    @MockK
    private lateinit var getFavoriteCountFlowUseCase: GetFavoriteCountFlowUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        sut = ProfileViewModel(
            getProfileUseCase = getProfileUseCase,
            getFavoriteCountFlowUseCase = getFavoriteCountFlowUseCase
        )
    }

    @Test
    fun `GIVEN failure from profile use case WHEN init intent is executed THEN state should be error`() =
        runTest {
            // Given
            coEvery {
                getProfileUseCase.invoke()
            } returns Either.Left(value = mockk())

            every {
                getFavoriteCountFlowUseCase.invoke()
            } returns flowOf(value = 0)

            sut.uiState.test {
                assert(awaitItem() == ProfileState.Idle)

                // When
                sut.handleIntent(ProfileIntent.Init)

                // Then
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Error)
            }
        }

    @Test
    fun `GIVEN success from use cases WHEN init intent is executed THEN state should be success`() =
        runTest {
            // Given
            coEvery {
                getProfileUseCase.invoke()
            } returns Either.Right(value = mockk(relaxed = true))

            every {
                getFavoriteCountFlowUseCase.invoke()
            } returns flowOf(value = 0)

            sut.uiState.test {
                assert(awaitItem() == ProfileState.Idle)

                // When
                sut.handleIntent(ProfileIntent.Init)

                // Then
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Success)
            }
        }

    @Test
    fun `GIVEN unexpected flow exception WHEN init intent is executed THEN state should be error`() =
        runTest {
            // Given
            coEvery {
                getProfileUseCase.invoke()
            } returns Either.Right(value = mockk(relaxed = true))

            every {
                getFavoriteCountFlowUseCase.invoke()
            } returns flow {
                throw Exception()
            }

            sut.uiState.test {
                assert(awaitItem() == ProfileState.Idle)

                // When
                sut.handleIntent(ProfileIntent.Init)

                // Then
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Error)
            }
        }

    @Test
    fun `GIVEN network failure WHEN retry intent is executed THEN state should be success`() =
        runTest {
            // Given
            coEvery {
                getProfileUseCase.invoke()
            } returns Either.Left(value = mockk()) andThen Either.Right(mockk(relaxed = true))

            every {
                getFavoriteCountFlowUseCase.invoke()
            } returns flowOf(value = 0)

            sut.uiState.test {
                assert(awaitItem() == ProfileState.Idle)

                // First failure try
                sut.handleIntent(ProfileIntent.Init)
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Error)

                // When
                sut.handleIntent(ProfileIntent.Retry)

                // Then
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Success)
            }
        }

    @Test
    fun `GIVEN initial failure WHEN retry intent is executed THEN state should be error`() =
        runTest {
            // Given
            coEvery {
                getProfileUseCase.invoke()
            } returns Either.Left(value = mockk()) andThen Either.Left(value = mockk())

            every {
                getFavoriteCountFlowUseCase.invoke()
            } returns flowOf(value = 0)

            sut.uiState.test {
                assert(awaitItem() == ProfileState.Idle)

                sut.handleIntent(ProfileIntent.Init)

                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Error)

                // When
                sut.handleIntent(ProfileIntent.Retry)

                // Then
                assert(awaitItem() == ProfileState.Loading)
                assert(awaitItem() is ProfileState.Error)
            }
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}