package com.vestis.feature.profile.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vestis.feature.profile.presentation.profile.component.ProfileErrorComponent
import com.vestis.feature.profile.presentation.profile.component.ProfileLoadingComponent
import com.vestis.feature.profile.presentation.profile.component.ProfileSuccessComponent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleIntent(intent = ProfileIntent.Init)
    }

    ProfileScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ProfileState.Idle,
        ProfileState.Loading -> ProfileLoadingComponent(
            modifier = modifier
        )

        is ProfileState.Error -> ProfileErrorComponent(
            message = state.text.asString(),
            onIntent = { onIntent(it) },
            modifier = modifier,
        )

        is ProfileState.Success -> ProfileSuccessComponent(
            state = state,
            modifier = modifier,
        )
    }
}