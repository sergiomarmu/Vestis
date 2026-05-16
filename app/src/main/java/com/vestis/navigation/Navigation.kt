package com.vestis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = Destination.PRODUCTS

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry
        ?.destination
        ?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.entries
                    .forEach { destination ->

                        val isSelected = currentDestination == destination.route

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentDestination != destination.route) {
                                    navController.navigate(route = destination.route) {
                                        popUpTo(
                                            id = navController.graph.findStartDestination().id
                                        ) { saveState = true }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
            }
        }
    ) { contentPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues = contentPadding)
        )
    }
}