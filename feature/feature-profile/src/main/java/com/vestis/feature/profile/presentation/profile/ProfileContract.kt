package com.vestis.feature.profile.presentation.profile

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.vestis.core.presentation.base.UiEffect
import com.vestis.core.presentation.base.UiIntent
import com.vestis.core.presentation.base.UiState

sealed interface ProfileState : UiState {

    data object Idle : ProfileState

    data object Loading : ProfileState

    @Immutable
    data class Success(
        val userName: String,
        val fullName: String,
        val email: String,
        val favouriteCount: Int
    ) : ProfileState


    data class Error(
        val message: String
    ) : ProfileState
}

sealed interface ProfileIntent : UiIntent {
    data object Init : ProfileIntent

    data object Retry : ProfileIntent
}

sealed interface ProfileEffect : UiEffect