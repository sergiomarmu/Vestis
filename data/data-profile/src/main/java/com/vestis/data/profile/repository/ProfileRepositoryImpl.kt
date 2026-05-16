package com.vestis.data.profile.repository

import com.vestis.core.data.exception.toExceptionDomain
import com.vestis.data.profile.network.datasource.ProfileNetworkDataSource
import com.vestis.data.profile.network.mapper.toDomain
import com.vestis.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileNetworkDataSource: ProfileNetworkDataSource
) : ProfileRepository {
    override suspend fun getProfile() = profileNetworkDataSource
        .getProfile()
        .map(
            ifLeft = {
                it.toExceptionDomain()
            },
            ifRight = {
                it.toDomain()
            }
        )

}