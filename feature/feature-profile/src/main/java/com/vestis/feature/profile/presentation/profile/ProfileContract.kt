package com.vestis.feature.profile.presentation.profile

import androidx.compose.runtime.Immutable
import com.vestis.core.presentation.base.UiEffect
import com.vestis.core.presentation.base.UiIntent
import com.vestis.core.presentation.base.UiState
import com.vestis.core.presentation.utils.text.UiText

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
        val text: UiText
    ) : ProfileState
}

sealed interface ProfileIntent : UiIntent {
    data object Init : ProfileIntent

    data object Retry : ProfileIntent
}

sealed interface ProfileEffect : UiEffect