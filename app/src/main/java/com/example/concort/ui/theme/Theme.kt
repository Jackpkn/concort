package com.example.concort.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Concort uses a custom dark theme only - no light theme for premium feel
private val ConcortColorScheme = darkColorScheme(
    // Primary
    primary = ConcortColors.Primary,
    onPrimary = ConcortColors.OnPrimary,
    primaryContainer = ConcortColors.PrimaryContainer,
    onPrimaryContainer = ConcortColors.PrimaryLight,
    
    // Secondary
    secondary = ConcortColors.Secondary,
    onSecondary = ConcortColors.OnSecondary,
    secondaryContainer = ConcortColors.SecondaryContainer,
    onSecondaryContainer = ConcortColors.SecondaryLight,
    
    // Tertiary
    tertiary = ConcortColors.Tertiary,
    onTertiary = Color.Black,
    tertiaryContainer = ConcortColors.TertiaryContainer,
    onTertiaryContainer = ConcortColors.Tertiary,
    
    // Background & Surface
    background = ConcortColors.Background,
    onBackground = ConcortColors.OnBackground,
    surface = ConcortColors.Surface,
    onSurface = ConcortColors.OnSurface,
    surfaceVariant = ConcortColors.SurfaceVariant,
    onSurfaceVariant = ConcortColors.OnSurfaceVariant,
    surfaceContainerLowest = ConcortColors.Background,
    surfaceContainerLow = ConcortColors.Surface,
    surfaceContainer = ConcortColors.SurfaceContainer,
    surfaceContainerHigh = ConcortColors.SurfaceContainerHigh,
    surfaceContainerHighest = ConcortColors.SurfaceContainerHigh,
    
    // Error
    error = ConcortColors.Error,
    onError = Color.White,
    errorContainer = Color(0xFF3D1515),
    onErrorContainer = ConcortColors.Error,
    
    // Outline
    outline = ConcortColors.Outline,
    outlineVariant = ConcortColors.OutlineVariant,
    
    // Others
    scrim = ConcortColors.Scrim,
    inverseSurface = ConcortColors.OnBackground,
    inverseOnSurface = ConcortColors.Background,
    inversePrimary = ConcortColors.PrimaryDark
)

@Composable
fun ConcortTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = ConcortColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to transparent for edge-to-edge
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = ConcortColors.Background.toArgb()
            
            // Use light icons on dark background
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}