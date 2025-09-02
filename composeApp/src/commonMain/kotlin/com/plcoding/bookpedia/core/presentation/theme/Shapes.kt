package com.plcoding.bookpedia.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Premium shape system for consistent rounded corners and visual hierarchy
val BookPediaShapes = Shapes(
    // Small shapes - for chips, small buttons
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    
    // Medium shapes - for cards, dialogs
    medium = RoundedCornerShape(16.dp),
    
    // Large shapes - for major UI elements
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shapes for specific components
object BookPediaCustomShapes {
    // Book cover shapes
    val BookCover = RoundedCornerShape(8.dp)
    val BookCoverLarge = RoundedCornerShape(12.dp)
    
    // Card shapes with different levels of emphasis
    val BookCard = RoundedCornerShape(20.dp)
    val FeatureCard = RoundedCornerShape(24.dp)
    val HeroCard = RoundedCornerShape(28.dp)
    
    // Button shapes
    val PrimaryButton = RoundedCornerShape(28.dp)
    val SecondaryButton = RoundedCornerShape(24.dp)
    val FloatingButton = RoundedCornerShape(16.dp)
    
    // Input shapes
    val SearchBar = RoundedCornerShape(28.dp)
    val TextField = RoundedCornerShape(12.dp)
    
    // Container shapes
    val BottomSheet = RoundedCornerShape(
        topStart = 32.dp,
        topEnd = 32.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    val TopContainer = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 32.dp,
        bottomEnd = 32.dp
    )
    
    // Special shapes for visual interest
    val CategoryChip = RoundedCornerShape(16.dp)
    val StatusBadge = RoundedCornerShape(12.dp)
    val RatingContainer = RoundedCornerShape(20.dp)
    
    // Navigation shapes
    val TabIndicator = RoundedCornerShape(16.dp)
    val NavigationContainer = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
}
