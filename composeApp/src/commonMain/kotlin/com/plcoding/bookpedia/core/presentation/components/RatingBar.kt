package com.plcoding.bookpedia.core.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.bookpedia.core.presentation.theme.BookPediaAnimations
import com.plcoding.bookpedia.core.presentation.theme.BookPediaColors
import kotlin.math.floor
import kotlin.math.round

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    size: Dp = 20.dp,
    spacing: Dp = 2.dp,
    showRatingText: Boolean = true,
    animate: Boolean = true,
    fillColor: Color = BookPediaColors.RatingStarYellow,
    emptyColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
) {
    val animatedRating by animateFloatAsState(
        targetValue = if (animate) rating.toFloat() else rating.toFloat(),
        animationSpec = tween(
            durationMillis = if (animate) BookPediaAnimations.DURATION_LONG else 0,
            easing = BookPediaAnimations.EmphasizedEasing
        ),
        label = "rating_animation"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Star rating display
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing / 2)
        ) {
            repeat(maxRating) { index ->
                StarIcon(
                    index = index,
                    rating = animatedRating,
                    size = size,
                    fillColor = fillColor,
                    emptyColor = emptyColor
                )
            }
        }

        // Rating text
        if (showRatingText) {
            Text(
                text = "${round(rating * 10) / 10}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = (size.value * 0.6).sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun StarIcon(
    index: Int,
    rating: Float,
    size: Dp,
    fillColor: Color,
    emptyColor: Color
) {
    val starState = when {
        rating >= index + 1 -> StarState.Filled
        rating >= index + 0.5 -> StarState.Half
        else -> StarState.Empty
    }

    // Animation for star fill
    val fillProgress by animateFloatAsState(
        targetValue = when (starState) {
            StarState.Filled -> 1f
            StarState.Half -> 0.5f
            StarState.Empty -> 0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
            visibilityThreshold = 0.01f
        ),
        label = "star_fill_$index"
    )

    Canvas(
        modifier = Modifier.size(size)
    ) {
        drawStar(
            center = Offset(this.size.width / 2, this.size.height / 2),
            radius = this.size.minDimension / 2 * 0.9f,
            fillProgress = fillProgress,
            fillColor = fillColor,
            emptyColor = emptyColor
        )
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    radius: Float,
    fillProgress: Float,
    fillColor: Color,
    emptyColor: Color
) {
    val path = createStarPath(center, radius)
    
    // Draw empty star background
    drawPath(
        path = path,
        color = emptyColor,
        style = androidx.compose.ui.graphics.drawscope.Fill
    )
    
    // Draw filled portion
    if (fillProgress > 0f) {
        val clipPath = Path().apply {
            addRect(
                androidx.compose.ui.geometry.Rect(
                    offset = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2 * fillProgress, radius * 2)
                )
            )
        }
        
        clipPath(clipPath) {
            drawPath(
                path = path,
                color = fillColor,
                style = androidx.compose.ui.graphics.drawscope.Fill
            )
        }
    }
}

private fun createStarPath(center: Offset, radius: Float): Path {
    val path = Path()
    val points = 5
    val innerRadius = radius * 0.4f
    
    // Calculate star points
    val angle = -kotlin.math.PI / 2 // Start from top
    val angleIncrement = kotlin.math.PI / points
    
    var isOuter = true
    for (i in 0 until points * 2) {
        val currentRadius = if (isOuter) radius else innerRadius
        val currentAngle = angle + (i * angleIncrement)
        
        val x = center.x + (currentRadius * kotlin.math.cos(currentAngle)).toFloat()
        val y = center.y + (currentRadius * kotlin.math.sin(currentAngle)).toFloat()
        
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
        
        isOuter = !isOuter
    }
    
    path.close()
    return path
}

@Composable
fun AnimatedRatingBar(
    targetRating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    size: Dp = 24.dp,
    spacing: Dp = 4.dp,
    showRatingText: Boolean = true,
    animationDelay: Int = 0,
    fillColor: Color = BookPediaColors.RatingStarYellow,
    emptyColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
) {
    var animationStarted by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(animationDelay.toLong())
        animationStarted = true
    }
    
    RatingBar(
        rating = if (animationStarted) targetRating else 0.0,
        modifier = modifier,
        maxRating = maxRating,
        size = size,
        spacing = spacing,
        showRatingText = showRatingText,
        animate = true,
        fillColor = fillColor,
        emptyColor = emptyColor
    )
}

@Composable
fun InteractiveRatingBar(
    rating: Double,
    onRatingChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    size: Dp = 32.dp,
    spacing: Dp = 4.dp,
    allowHalfStars: Boolean = true,
    fillColor: Color = BookPediaColors.RatingStarYellow,
    emptyColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
) {
    // Interactive rating implementation would go here
    // For now, just display the rating
    RatingBar(
        rating = rating,
        modifier = modifier,
        maxRating = maxRating,
        size = size,
        spacing = spacing,
        showRatingText = false,
        fillColor = fillColor,
        emptyColor = emptyColor
    )
}

private enum class StarState {
    Empty, Half, Filled
}
