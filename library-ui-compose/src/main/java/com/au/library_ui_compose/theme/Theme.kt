package com.au.library_ui_compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    onPrimary = DarkOnPrimaryColor,
    background = DarkSurfaceColor,
    outline = DarkOutlineColor
)

private val LightColorScheme = lightColorScheme(
    onPrimary = LightOnPrimaryColor,
    background = LightSurfaceColor,
    outline = LightOutlineColor
)

@Composable
fun ObjectSizeEstimationAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}