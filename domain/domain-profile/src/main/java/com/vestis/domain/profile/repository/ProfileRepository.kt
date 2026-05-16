package com.vestis.domain.profile.repository

import com.vestis.core.common.either.Either
import com.vestis.core.domain.DomainException
import com.vestis.domain.profile.model.ProfileModel

interface ProfileRepository {

    suspend fun getProfile(): Either<DomainException, ProfileModel>
}