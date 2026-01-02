package com.example.concort.ui.theme

import androidx.compose.ui.graphics.Color

// Premium Romantic Color Palette
object ConcortColors {
    // Primary - Warm Rose/Coral
    val Primary = Color(0xFFE85D75)           // Rose coral
    val PrimaryDark = Color(0xFFB33D55)       // Deep rose
    val PrimaryLight = Color(0xFFFFA4B2)      // Light rose
    val PrimaryContainer = Color(0xFF3D1520)  // Dark rose container
    
    // Secondary - Elegant Purple
    val Secondary = Color(0xFF7C3AED)         // Vibrant purple
    val SecondaryDark = Color(0xFF5B21B6)     // Deep purple
    val SecondaryLight = Color(0xFFC4B5FD)    // Light purple
    val SecondaryContainer = Color(0xFF2D1B4E) // Dark purple container
    
    // Tertiary - Warm Amber
    val Tertiary = Color(0xFFF59E0B)          // Amber gold
    val TertiaryContainer = Color(0xFF3D2607) // Dark amber container
    
    // Backgrounds - Deep Dark Theme
    val Background = Color(0xFF0A0A0F)        // Almost black
    val Surface = Color(0xFF12121A)           // Card surface
    val SurfaceVariant = Color(0xFF1A1A26)    // Elevated surface
    val SurfaceContainer = Color(0xFF1E1E2A)  // Higher elevation
    val SurfaceContainerHigh = Color(0xFF252533) // Highest elevation
    
    // Status Colors
    val Success = Color(0xFF10B981)           // Emerald green
    val Warning = Color(0xFFF59E0B)           // Amber
    val Error = Color(0xFFEF4444)             // Red
    val Info = Color(0xFF3B82F6)              // Blue
    
    // Text Colors
    val OnBackground = Color(0xFFF5F5F7)      // Almost white
    val OnBackgroundMuted = Color(0xFF9CA3AF) // Gray text
    val OnPrimary = Color(0xFFFFFFFF)         // White
    val OnSecondary = Color(0xFFFFFFFF)       // White
    val OnSurface = Color(0xFFF5F5F7)         // Almost white
    val OnSurfaceVariant = Color(0xFFB0B0B8)  // Muted text
    
    // Outline
    val Outline = Color(0xFF2E2E3A)           // Subtle border
    val OutlineVariant = Color(0xFF3D3D4D)    // Stronger border
    
    // Scrim
    val Scrim = Color(0x99000000)             // 60% black overlay
}

// Gradient Definitions
val MatchGradient = listOf(
    ConcortColors.Primary,
    ConcortColors.Secondary
)

val PremiumGradient = listOf(
    Color(0xFFE85D75),
    Color(0xFF9333EA),
    Color(0xFF7C3AED)
)

val CardGradient = listOf(
    ConcortColors.Surface,
    ConcortColors.SurfaceVariant
)

val SuccessGradient = listOf(
    Color(0xFF10B981),
    Color(0xFF059669)
)

// Legacy colors for compatibility
val Purple80 = ConcortColors.SecondaryLight
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = ConcortColors.PrimaryLight

val Purple40 = ConcortColors.Secondary
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = ConcortColors.Primary