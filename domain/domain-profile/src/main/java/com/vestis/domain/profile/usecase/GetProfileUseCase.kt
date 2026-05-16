package com.vestis.domain.profile.usecase

import com.vestis.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.getProfile()
}