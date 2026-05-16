package com.vestis.navigation

import FavoritesListScreen
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
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.PRODUCTS -> ProductListScreen()
                    Destination.FAVORITES -> FavoritesListScreen()
                    Destination.PROFILE -> ProfileScreen()
                }
            }
        }
    }
}