package com.vestis.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Purple600,
    onPrimary = Color.White,
    primaryContainer = Purple100,
    onPrimaryContainer = Purple600,

    secondary = Blue600,
    onSecondary = Color.White,
    secondaryContainer = Blue100,
    onSecondaryContainer = Blue600,

    tertiary = Red600,
    onTertiary = Color.White,
    tertiaryContainer = Red100,
    onTertiaryContainer = Red600,

    error = Red500,
    onError = Color.White,

    background = Background,
    onBackground = Gray900,

    surface = Surface,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,

    outline = Gray300,
    outlineVariant = Gray200
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    primaryContainer = Purple600,
    onPrimaryContainer = Purple100,

    secondary = Blue500,
    onSecondary = Color.White,
    secondaryContainer = Blue600,
    onSecondaryContainer = Blue100,

    tertiary = Red500,
    onTertiary = Color.White,
    tertiaryContainer = Red600,
    onTertiaryContainer = Red100,

    error = Red500,
    onError = Color.White,

    background = Gray900,
    onBackground = Gray50,

    surface = Gray700,
    onSurface = Gray50,
    surfaceVariant = Gray600,
    onSurfaceVariant = Gray300,

    outline = Gray500,
    outlineVariant = Gray600
)

@Composable
fun VestisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}