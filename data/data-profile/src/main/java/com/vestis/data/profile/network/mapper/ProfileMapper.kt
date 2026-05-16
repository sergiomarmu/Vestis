package com.vestis.data.profile.network.mapper

import com.vestis.data.profile.network.dto.ProfileDTO
import com.vestis.domain.profile.model.ProfileModel


fun ProfileDTO.toDomain() = ProfileModel(
    id = this.id,
    email = this.email,
    username = this.username,
    name = ProfileModel.Name(
        firstName = this.name.firstName,
        lastName = this.name.lastName
    )
)
