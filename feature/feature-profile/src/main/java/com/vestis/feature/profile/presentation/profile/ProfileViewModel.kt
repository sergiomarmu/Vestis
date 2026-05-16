@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vestis.feature.profile.presentation.profile

import androidx.lifecycle.viewModelScope
import com.vestis.core.presentation.base.BaseMviViewModel
import com.vestis.core.presentation.mapper.toUiMessage
import com.vestis.domain.favorite.usecase.GetFavoriteCountFlowUseCase
import com.vestis.domain.profile.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getFavoriteCountFlowUseCase: GetFavoriteCountFlowUseCase
) : BaseMviViewModel<ProfileState, ProfileIntent, ProfileEffect>(
    initialState = ProfileState.Idle
) {
    private val refresh = MutableSharedFlow<Unit>(replay = 0)

    override fun handleIntent(
        intent: ProfileIntent
    ) {
        when (intent) {
            ProfileIntent.Init -> processInit()
            ProfileIntent.Retry -> processRetry()
        }
    }

    private fun processInit() {
        if (uiState.value is ProfileState.Success) return

        updateState { ProfileState.Loading }

        refresh
            .onStart { emit(Unit) }
            .flatMapLatest {
                combine(
                    flow {
                        emit(getProfileUseCase.invoke())
                    },
                    getFavoriteCountFlowUseCase.invoke()
                ) { userResult, favoriteCount ->

                    userResult.fold(
                        ifLeft = {
                            ProfileState.Error(
                                message = it.toUiMessage()
                            )
                        },
                        ifRight = {
                            ProfileState.Success(
                                userName = it.username,
                                fullName = "${it.name.firstName} ${it.name.lastName}",
                                email = it.email,
                                favouriteCount = favoriteCount
                            )
                        }
                    )

                }.onEach { newState ->
                    updateState { newState }
                }.catch { error ->
                    updateState {
                        ProfileState.Error(message = error.message ?: "Unknown error occurred")
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun processRetry() {
        updateState { ProfileState.Loading }

        viewModelScope.launch {
            refresh.emit(Unit)
        }
    }
}