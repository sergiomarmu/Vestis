package com.vestis.feature.profile.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ProfileHeader(
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
            contentDescription = "User avatar",
            modifier = Modifier.size(64.dp),
        )

        Text(
            text = state.userName,
            fontSize = 18.sp,
        )

        Text(
            text = state.fullName,
            fontSize = 26.sp,
        )

        Text(
            text = state.email,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun ProfileFavoriteSection(
    counter: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Favorites",
            fontSize = 18.sp,
        )

        Text(
            text = counter.toString(),
            fontSize = 18.sp,
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