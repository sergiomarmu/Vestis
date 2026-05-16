package com.vestis.feature.profile.presentation.profile.component.parameterprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.vestis.feature.profile.presentation.profile.ProfileState


class ProfileSuccessStateParameterProvider : PreviewParameterProvider<ProfileState.Success> {
    override val values = sequenceOf(
        ProfileState.Success(
            userName = "Username",
            fullName = "Name FirstName LastName",
            email = "email@email.com",
            favouriteCount = 8
        ),
        ProfileState.Success(
            userName = "Username2",
            fullName = "Name FirstName LastName",
            email = "example@example.com",
            favouriteCount = 0
        )
    )
}