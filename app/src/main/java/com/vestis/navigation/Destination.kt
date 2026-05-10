package com.vestis.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    PRODUCTS(
        route = "products",
        label = "Products",
        icon = Icons.AutoMirrored.Filled.List,
        contentDescription = "Products"
    ),
    FAVORITES(
        route = "favorites",
        label = "Favorites",
        icon = Icons.Default.Favorite,
        contentDescription = "Favorites"
    ),
    PROFILE(
        route = "profile",
        label = "Profile",
        icon = Icons.Default.AccountBox,
        contentDescription = "Profile"
    )
}