package com.plcoding.bookpedia.core.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.bookpedia.core.presentation.theme.*

@Composable
fun EmptyState(
    type: EmptyStateType,
    modifier: Modifier = Modifier,
    onActionClick: (() -> Unit)? = null,
    customTitle: String? = null,
    customSubtitle: String? = null,
    customActionText: String? = null
) {
    val config = getEmptyStateConfig(type)
    
    // Animation states
    var animationStarted by remember { mutableStateOf(false) }
    
    val illustrationAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = BookPediaAnimations.EmphasizedEasing
        ),
        label = "illustration_alpha"
    )
    
    val contentAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 400,
            easing = BookPediaAnimations.FastOutSlowInEasing
        ),
        label = "content_alpha"
    )
    
    val illustrationScale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "illustration_scale"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        animationStarted = true
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Custom illustration
        EmptyStateIllustration(
            type = type,
            modifier = Modifier
                .size(200.dp)
                .alpha(illustrationAlpha)
                .scale(illustrationScale)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Title
        Text(
            text = customTitle ?: config.title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.alpha(contentAlpha)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Subtitle
        Text(
            text = customSubtitle ?: config.subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .alpha(contentAlpha)
                .fillMaxWidth(0.8f)
        )
        
        // Action button
        if (onActionClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            
            PremiumButton(
                text = customActionText ?: config.actionText,
                onClick = onActionClick,
                style = ButtonStyle.Primary,
                size = ButtonSize.Medium,
                modifier = Modifier.alpha(contentAlpha)
            )
        }
    }
}

@Composable
private fun EmptyStateIllustration(
    type: EmptyStateType,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = BookPediaColors.SecondaryDark
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    
    // Floating animation
    val floatingOffset by rememberInfiniteTransition(label = "floating").animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_offset"
    )
    
    Canvas(
        modifier = modifier
    ) {
        when (type) {
            EmptyStateType.NoBooks -> drawNoBooksIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
            EmptyStateType.NoSearchResults -> drawNoSearchResultsIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
            EmptyStateType.NoFavorites -> drawNoFavoritesIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
            EmptyStateType.EmptyCart -> drawEmptyCartIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
            EmptyStateType.NetworkError -> drawNetworkErrorIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
            EmptyStateType.Generic -> drawGenericIllustration(primaryColor, secondaryColor, surfaceColor, floatingOffset)
        }
    }
}

private fun DrawScope.drawNoBooksIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // Bookshelf
    drawRect(
        color = surfaceColor,
        topLeft = Offset(centerX - 80.dp.toPx(), centerY + 20.dp.toPx()),
        size = androidx.compose.ui.geometry.Size(160.dp.toPx(), 20.dp.toPx())
    )
    
    // Empty book slots
    repeat(3) { index ->
        val bookX = centerX - 60.dp.toPx() + (index * 40.dp.toPx())
        drawRect(
            color = primaryColor.copy(alpha = 0.3f),
            topLeft = Offset(bookX, centerY - 40.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(30.dp.toPx(), 60.dp.toPx())
        )
    }
    
    // Dust particles
    repeat(8) { index ->
        val angle = (index * 45f) * (kotlin.math.PI / 180f)
        val radius = 60.dp.toPx() + (index * 5.dp.toPx())
        val x = centerX + (kotlin.math.cos(angle) * radius).toFloat()
        val y = centerY + (kotlin.math.sin(angle) * radius).toFloat() + floatingOffset * 0.5f
        
        drawCircle(
            color = secondaryColor.copy(alpha = 0.4f),
            radius = 3.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawNoSearchResultsIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // Magnifying glass
    val glassRadius = 40.dp.toPx()
    drawCircle(
        color = Color.Transparent,
        radius = glassRadius,
        center = Offset(centerX - 10.dp.toPx(), centerY - 10.dp.toPx()),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), pathEffect = null)
    )
    
    // Handle
    drawLine(
        color = primaryColor,
        start = Offset(centerX + 20.dp.toPx(), centerY + 20.dp.toPx()),
        end = Offset(centerX + 40.dp.toPx(), centerY + 40.dp.toPx()),
        strokeWidth = 4.dp.toPx()
    )
    
    // Question marks floating around
    // This would typically use text drawing, simplified here as circles
    repeat(3) { index ->
        val angle = (index * 120f) * (kotlin.math.PI / 180f)
        val radius = 70.dp.toPx()
        val x = centerX + (kotlin.math.cos(angle) * radius).toFloat()
        val y = centerY + (kotlin.math.sin(angle) * radius).toFloat() + floatingOffset * 0.3f
        
        drawCircle(
            color = secondaryColor.copy(alpha = 0.6f),
            radius = 8.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawNoFavoritesIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // Broken heart shape
    val heartPath = Path().apply {
        val heartSize = 60.dp.toPx()
        moveTo(centerX, centerY + heartSize * 0.3f)
        
        // Left curve
        cubicTo(
            centerX - heartSize * 0.5f, centerY - heartSize * 0.1f,
            centerX - heartSize * 0.5f, centerY - heartSize * 0.5f,
            centerX - heartSize * 0.2f, centerY - heartSize * 0.5f
        )
        
        // Top left arc
        cubicTo(
            centerX - heartSize * 0.1f, centerY - heartSize * 0.7f,
            centerX + heartSize * 0.1f, centerY - heartSize * 0.7f,
            centerX + heartSize * 0.2f, centerY - heartSize * 0.5f
        )
        
        // Right curve
        cubicTo(
            centerX + heartSize * 0.5f, centerY - heartSize * 0.5f,
            centerX + heartSize * 0.5f, centerY - heartSize * 0.1f,
            centerX, centerY + heartSize * 0.3f
        )
        close()
    }
    
    drawPath(
        path = heartPath,
        color = primaryColor.copy(alpha = 0.3f)
    )
    
    // Crack in the heart
    drawLine(
        color = surfaceColor,
        start = Offset(centerX, centerY - 30.dp.toPx()),
        end = Offset(centerX + 5.dp.toPx(), centerY + 20.dp.toPx()),
        strokeWidth = 3.dp.toPx()
    )
}

private fun DrawScope.drawEmptyCartIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // Shopping cart outline
    val cartPath = Path().apply {
        moveTo(centerX - 40.dp.toPx(), centerY - 20.dp.toPx())
        lineTo(centerX + 20.dp.toPx(), centerY - 20.dp.toPx())
        lineTo(centerX + 15.dp.toPx(), centerY + 10.dp.toPx())
        lineTo(centerX - 35.dp.toPx(), centerY + 10.dp.toPx())
        close()
    }
    
    drawPath(
        path = cartPath,
        color = Color.Transparent,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
    )
    
    // Cart wheels
    drawCircle(
        color = primaryColor,
        radius = 8.dp.toPx(),
        center = Offset(centerX - 20.dp.toPx(), centerY + 25.dp.toPx())
    )
    drawCircle(
        color = primaryColor,
        radius = 8.dp.toPx(),
        center = Offset(centerX + 5.dp.toPx(), centerY + 25.dp.toPx())
    )
    
    // Floating "empty" particles
    repeat(6) { index ->
        val angle = (index * 60f) * (kotlin.math.PI / 180f)
        val radius = 50.dp.toPx() + (index * 3.dp.toPx())
        val x = centerX + (kotlin.math.cos(angle) * radius).toFloat()
        val y = centerY + (kotlin.math.sin(angle) * radius).toFloat() + floatingOffset * 0.4f
        
        drawCircle(
            color = secondaryColor.copy(alpha = 0.5f),
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawNetworkErrorIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // WiFi symbol with X
    repeat(3) { index ->
        val radius = (index + 1) * 20.dp.toPx()
        drawCircle(
            color = primaryColor.copy(alpha = 0.3f - index * 0.1f),
            radius = radius,
            center = Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
        )
    }
    
    // X mark
    drawLine(
        color = Color.Red,
        start = Offset(centerX - 15.dp.toPx(), centerY - 15.dp.toPx()),
        end = Offset(centerX + 15.dp.toPx(), centerY + 15.dp.toPx()),
        strokeWidth = 4.dp.toPx()
    )
    drawLine(
        color = Color.Red,
        start = Offset(centerX + 15.dp.toPx(), centerY - 15.dp.toPx()),
        end = Offset(centerX - 15.dp.toPx(), centerY + 15.dp.toPx()),
        strokeWidth = 4.dp.toPx()
    )
}

private fun DrawScope.drawGenericIllustration(
    primaryColor: Color,
    secondaryColor: Color,
    surfaceColor: Color,
    floatingOffset: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2 + floatingOffset
    
    // Simple geometric pattern
    drawCircle(
        color = primaryColor.copy(alpha = 0.3f),
        radius = 50.dp.toPx(),
        center = Offset(centerX, centerY)
    )
    
    drawCircle(
        color = secondaryColor.copy(alpha = 0.5f),
        radius = 30.dp.toPx(),
        center = Offset(centerX, centerY)
    )
    
    drawCircle(
        color = surfaceColor,
        radius = 15.dp.toPx(),
        center = Offset(centerX, centerY)
    )
}

private data class EmptyStateConfig(
    val title: String,
    val subtitle: String,
    val actionText: String
)

private fun getEmptyStateConfig(type: EmptyStateType): EmptyStateConfig {
    return when (type) {
        EmptyStateType.NoBooks -> EmptyStateConfig(
            title = "No Books Found",
            subtitle = "We couldn't find any books at the moment. Please check your connection and try again.",
            actionText = "Retry"
        )
        EmptyStateType.NoSearchResults -> EmptyStateConfig(
            title = "No Results Found",
            subtitle = "We couldn't find any books matching your search. Try different keywords or browse our categories.",
            actionText = "Browse Categories"
        )
        EmptyStateType.NoFavorites -> EmptyStateConfig(
            title = "No Favorite Books Yet",
            subtitle = "Start building your reading list by adding books you love to your favorites.",
            actionText = "Discover Books"
        )
        EmptyStateType.EmptyCart -> EmptyStateConfig(
            title = "Your Cart is Empty",
            subtitle = "Add some books to your cart to get started with your next reading adventure.",
            actionText = "Shop Books"
        )
        EmptyStateType.NetworkError -> EmptyStateConfig(
            title = "Connection Problem",
            subtitle = "Please check your internet connection and try again.",
            actionText = "Retry"
        )
        EmptyStateType.Generic -> EmptyStateConfig(
            title = "Nothing Here Yet",
            subtitle = "This space is waiting to be filled with amazing content.",
            actionText = "Get Started"
        )
    }
}

enum class EmptyStateType {
    NoBooks,
    NoSearchResults,
    NoFavorites,
    EmptyCart,
    NetworkError,
    Generic
}
