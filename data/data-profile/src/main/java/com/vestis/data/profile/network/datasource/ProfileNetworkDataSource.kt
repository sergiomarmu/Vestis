package com.vestis.data.profile.network.datasource

import com.vestis.core.common.either.Either
import com.vestis.core.data.exception.DataException
import com.vestis.data.profile.network.dto.ProfileDTO

interface ProfileNetworkDataSource {
    suspend fun getProfile(): Either<DataException, ProfileDTO>
}