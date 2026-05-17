package com.vestis.navigation

import FavoritesListScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vestis.feature.products.presentation.list.ProductListScreen
import com.vestis.feature.profile.presentation.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route,
        enterTransition = { fadeIn(animationSpec = tween(150)) },
        exitTransition = { fadeOut(animationSpec = tween(150)) },
        popEnterTransition = { fadeIn(animationSpec = tween(150)) },
        popExitTransition = { fadeOut(animationSpec = tween(150)) }
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.PRODUCTS -> ProductListScreen(
                        snackbarHostState = snackbarHostState
                    )

                    Destination.FAVORITES -> FavoritesListScreen(
                        snackbarHostState = snackbarHostState
                    )

                    Destination.PROFILE -> ProfileScreen()
                }
            }
        }
    }
}