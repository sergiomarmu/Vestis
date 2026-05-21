package com.vestis.feature.profile.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vestis.feature.profile.R
import com.vestis.feature.profile.presentation.profile.ProfileState
import com.vestis.feature.profile.presentation.profile.component.parameterprovider.ProfileSuccessStateParameterProvider

@Composable
fun ProfileSuccessComponent(
    state: ProfileState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileHeader(
            state = state
        )

        ProfileFavoriteSection(
            counter = state.favouriteCount
        )
    }

}

@Composable
private fun ProfileHeader(
    state: ProfileState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = stringResource(id = R.string.profile_user_avatar_content_description),
            modifier = Modifier.size(64.dp),
        )

        Text(
            text = state.userName,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = state.fullName,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = state.email,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProfileFavoriteSection(
    counter: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.profile_favorites_label),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = counter.toString(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
private fun ProfileSuccessComponentPreview(
    @PreviewParameter(provider = ProfileSuccessStateParameterProvider::class) state: ProfileState.Success
) {
    ProfileSuccessComponent(
        state = state
    )
}
