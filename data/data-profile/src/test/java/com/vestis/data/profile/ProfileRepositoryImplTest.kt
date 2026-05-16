package com.vestis.data.profile

import com.vestis.core.common.either.Either
import com.vestis.core.data.exception.DataException
import com.vestis.core.domain.DomainException
import com.vestis.data.profile.network.datasource.ProfileNetworkDataSource
import com.vestis.data.profile.network.dto.ProfileDTO
import com.vestis.data.profile.repository.ProfileRepositoryImpl
import com.vestis.domain.profile.model.ProfileModel
import com.vestis.domain.profile.repository.ProfileRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {

    private lateinit var sut: ProfileRepository

    @MockK
    private lateinit var profileNetworkDataSource: ProfileNetworkDataSource


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ProfileRepositoryImpl(
            profileNetworkDataSource = profileNetworkDataSource,
        )
    }

    @Test
    fun `GIVEN a network connection WHEN getProfile is executed THEN fetch from network`() =
        runTest {
            // Given
            coEvery { profileNetworkDataSource.getProfile() } returns Either.Right(value = mockk<ProfileDTO>(relaxed = true))

            // When
            val result = sut.getProfile()

            // Then
            assert(result.rightOrNull() != null)
            assert(result.rightOrNull() is ProfileModel)

            coVerify(exactly = 1) {
                profileNetworkDataSource.getProfile()
            }
        }

    @Test
    fun `GIVEN a no network connection WHEN getProfile is executed THEN throw exception`() =
        runTest {
            // Given
            coEvery { profileNetworkDataSource.getProfile() } returns Either.Left(value = DataException.Network.Unreachable())

            // When
            val result = sut.getProfile()

            // Then
            assert(result.leftOrNull() is DomainException.Network.NoConnection)

            coVerify(exactly = 1) {
                profileNetworkDataSource.getProfile()
            }
        }
}