package com.plcoding.bookpedia.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// Premium Book-themed Color Palette
object BookPediaColors {
    // Primary - Deep Navy inspired by classic book covers
    val PrimaryDark = Color(0xFF1A237E)
    val PrimaryLight = Color(0xFF3949AB)
    val PrimaryContainer = Color(0xFF303F9F)
    
    // Secondary - Warm Gold for accents
    val SecondaryDark = Color(0xFFFFD700)
    val SecondaryLight = Color(0xFFFFB300)
    val SecondaryContainer = Color(0xFFFFF8E1)
    
    // Background - Rich dark and warm light tones
    val BackgroundDark = Color(0xFF0A0E1A)
    val BackgroundLight = Color(0xFFF8F6F3)
    val SurfaceDark = Color(0xFF1C1B1F)
    val SurfaceLight = Color(0xFFFFFFFF)
    
    // Surface variants for cards and elevated content
    val SurfaceVariantDark = Color(0xFF2D2A33)
    val SurfaceVariantLight = Color(0xFFF3F1F5)
    
    // Text colors
    val OnPrimaryDark = Color(0xFFFFFFFF)
    val OnPrimaryLight = Color(0xFFFFFFFF)
    val OnBackgroundDark = Color(0xFFE4E1E6)
    val OnBackgroundLight = Color(0xFF1C1B1F)
    val OnSurfaceDark = Color(0xFFE4E1E6)
    val OnSurfaceLight = Color(0xFF1C1B1F)
    
    // Error colors
    val ErrorDark = Color(0xFFCF6679)
    val ErrorLight = Color(0xFFD32F2F)
    
    // Special book-themed colors
    val BookSpineGold = Color(0xFFB8860B)
    val BookPaperCream = Color(0xFFFAF0E6)
    val VintageBookBrown = Color(0xFF8B4513)
    val ReadingGreen = Color(0xFF2E7D32)
    val WishlistPink = Color(0xFFE91E63)
    val RatingStarYellow = Color(0xFFFFC107)
    
    // Gradient colors for hero sections
    val GradientStart = Color(0xFF1A237E)
    val GradientMiddle = Color(0xFF283593)
    val GradientEnd = Color(0xFF3949AB)
    
    // Semantic colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Info = Color(0xFF2196F3)
}

private val DarkColorScheme = darkColorScheme(
    primary = BookPediaColors.PrimaryLight,
    onPrimary = BookPediaColors.OnPrimaryDark,
    primaryContainer = BookPediaColors.PrimaryContainer,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = BookPediaColors.SecondaryDark,
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = BookPediaColors.SecondaryDark,
    tertiary = BookPediaColors.BookSpineGold,
    onTertiary = Color(0xFF000000),
    background = BookPediaColors.BackgroundDark,
    onBackground = BookPediaColors.OnBackgroundDark,
    surface = BookPediaColors.SurfaceDark,
    onSurface = BookPediaColors.OnSurfaceDark,
    surfaceVariant = BookPediaColors.SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFC7C5D0),
    error = BookPediaColors.ErrorDark,
    onError = Color(0xFF000000),
    outline = Color(0xFF928F99),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    surfaceContainerHighest = Color(0xFF36343B)
)

private val LightColorScheme = lightColorScheme(
    primary = BookPediaColors.PrimaryDark,
    onPrimary = BookPediaColors.OnPrimaryLight,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = BookPediaColors.PrimaryDark,
    secondary = BookPediaColors.SecondaryLight,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = BookPediaColors.SecondaryContainer,
    onSecondaryContainer = Color(0xFF1A1A1A),
    tertiary = BookPediaColors.VintageBookBrown,
    onTertiary = Color(0xFFFFFFFF),
    background = BookPediaColors.BackgroundLight,
    onBackground = BookPediaColors.OnBackgroundLight,
    surface = BookPediaColors.SurfaceLight,
    onSurface = BookPediaColors.OnSurfaceLight,
    surfaceVariant = BookPediaColors.SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF47464F),
    error = BookPediaColors.ErrorLight,
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFF777680),
    surfaceContainer = Color(0xFFF3F1F5),
    surfaceContainerHigh = Color(0xFFEDE7F0),
    surfaceContainerHighest = Color(0xFFE7E1E5)
)

@Composable
fun BookPediaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalBookPediaColors provides colorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = BookPediaTypography,
            shapes = BookPediaShapes,
            content = content
        )
    }
}

// Provide access to our custom colors
val LocalBookPediaColors = androidx.compose.runtime.staticCompositionLocalOf {
    DarkColorScheme
}

@Composable
fun bookPediaColors() = LocalBookPediaColors.current
