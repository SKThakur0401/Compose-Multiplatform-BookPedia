package com.plcoding.bookpedia.book.presentation.splash_screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.bookpedia.core.presentation.theme.BookPediaAnimations
import com.plcoding.bookpedia.core.presentation.theme.BookPediaColors
import com.plcoding.bookpedia.core.presentation.theme.BookPediaCustomTypography
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.math.cos

@Composable
fun SplashScreen(gotoBookListScreen: () -> Unit) {
    // Animation states
    var animationStarted by remember { mutableStateOf(false) }
    
    // Book opening animation progress
    val bookOpenProgress by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = BookPediaAnimations.EmphasizedEasing
        ),
        label = "book_open"
    )
    
    // Logo reveal animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (bookOpenProgress > 0.3f) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = BookPediaAnimations.FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )
    
    val logoScale by animateFloatAsState(
        targetValue = if (bookOpenProgress > 0.3f) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logo_scale"
    )
    
    // Subtitle animation
    val subtitleAlpha by animateFloatAsState(
        targetValue = if (bookOpenProgress > 0.6f) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = BookPediaAnimations.FastOutSlowInEasing
        ),
        label = "subtitle_alpha"
    )
    
    // Particle animation
    val particleAnimation by rememberInfiniteTransition(label = "particles").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )
    
    LaunchedEffect(Unit) {
        delay(300) // Small delay before starting animation
        animationStarted = true
        delay(2200) // Total duration including animations
        gotoBookListScreen()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        BookPediaColors.GradientStart,
                        BookPediaColors.GradientMiddle,
                        BookPediaColors.GradientEnd,
                        BookPediaColors.BackgroundDark
                    ),
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        
        // Animated particles background
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawParticles(particleAnimation)
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            // Animated book opening
            BookOpeningAnimation(
                progress = bookOpenProgress,
                modifier = Modifier.size(200.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // App logo/title
            Text(
                text = "BookPedia",
                style = BookPediaCustomTypography.SplashTitle,
                color = Color.White,
                modifier = Modifier
                    .alpha(logoAlpha)
                    .scale(logoScale)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle
            Text(
                text = "Discover Your Next Great Read",
                style = BookPediaCustomTypography.SplashSubtitle,
                color = BookPediaColors.SecondaryDark,
                modifier = Modifier.alpha(subtitleAlpha)
            )
            
        }
    }
}

@Composable
private fun BookOpeningAnimation(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val bookWidth = size.width * 0.8f
        val bookHeight = size.height * 0.6f
        
        // Book spine
        val spineRect = androidx.compose.ui.geometry.Rect(
            offset = Offset(centerX - 8.dp.toPx(), centerY - bookHeight / 2),
            size = androidx.compose.ui.geometry.Size(16.dp.toPx(), bookHeight)
        )
        
        drawRect(
            color = BookPediaColors.VintageBookBrown,
            topLeft = spineRect.topLeft,
            size = spineRect.size
        )
        
        // Left page (rotates from closed to open)
        val leftPageRotation = -90f + (progress * 90f)
        rotate(
            degrees = leftPageRotation,
            pivot = Offset(centerX, centerY)
        ) {
            drawRect(
                color = BookPediaColors.BookPaperCream,
                topLeft = Offset(centerX - bookWidth / 2, centerY - bookHeight / 2),
                size = androidx.compose.ui.geometry.Size(bookWidth / 2, bookHeight)
            )
            
            // Left page shadow
            drawRect(
                color = Color.Black.copy(alpha = 0.1f * progress),
                topLeft = Offset(centerX - bookWidth / 2, centerY - bookHeight / 2),
                size = androidx.compose.ui.geometry.Size(bookWidth / 2, bookHeight)
            )
        }
        
        // Right page (rotates from closed to open)
        val rightPageRotation = 90f - (progress * 90f)
        rotate(
            degrees = rightPageRotation,
            pivot = Offset(centerX, centerY)
        ) {
            drawRect(
                color = BookPediaColors.BookPaperCream,
                topLeft = Offset(centerX, centerY - bookHeight / 2),
                size = androidx.compose.ui.geometry.Size(bookWidth / 2, bookHeight)
            )
            
            // Right page shadow
            drawRect(
                color = Color.Black.copy(alpha = 0.1f * progress),
                topLeft = Offset(centerX, centerY - bookHeight / 2),
                size = androidx.compose.ui.geometry.Size(bookWidth / 2, bookHeight)
            )
        }
        
        // Book pages effect (stacked pages)
        for (i in 1..5) {
            val pageOffset = i * 2.dp.toPx()
            val pageAlpha = 0.8f - (i * 0.1f)
            
            drawRect(
                color = BookPediaColors.BookPaperCream.copy(alpha = pageAlpha),
                topLeft = Offset(centerX - 6.dp.toPx(), centerY - bookHeight / 2 - pageOffset),
                size = androidx.compose.ui.geometry.Size(12.dp.toPx(), bookHeight + pageOffset * 2)
            )
        }
        
        // Sparkle effects when book opens
        if (progress > 0.7f) {
            val sparkles = listOf(
                Offset(centerX - 60.dp.toPx(), centerY - 40.dp.toPx()),
                Offset(centerX + 60.dp.toPx(), centerY - 40.dp.toPx()),
                Offset(centerX - 40.dp.toPx(), centerY + 50.dp.toPx()),
                Offset(centerX + 40.dp.toPx(), centerY + 50.dp.toPx()),
            )
            
            sparkles.forEach { sparklePos ->
                val sparkleSize = (progress - 0.7f) * 3f * 8.dp.toPx()
                drawCircle(
                    color = BookPediaColors.SecondaryDark.copy(alpha = progress - 0.7f),
                    radius = sparkleSize,
                    center = sparklePos
                )
            }
        }
    }
}

private fun DrawScope.drawParticles(animationProgress: Float) {
    val particleCount = 20
    val particles = (0 until particleCount).map { index ->
        val angle = (index.toFloat() / particleCount) * 2 * kotlin.math.PI
        val baseRadius = 200.dp.toPx()
        val radiusVariation = sin(animationProgress * 2 * kotlin.math.PI + angle) * 50.dp.toPx()
        val radius = baseRadius + radiusVariation
        
        val x = size.width / 2 + cos(angle + animationProgress * kotlin.math.PI) * radius
        val y = size.height / 2 + sin(angle + animationProgress * kotlin.math.PI) * radius
        
        val alpha = (sin(animationProgress * 4 * kotlin.math.PI + angle) + 1) / 2 * 0.3f
        
        Offset(x.toFloat(), y.toFloat()) to alpha
    }
    
    particles.forEach { (position, alpha) ->
        drawCircle(
            color = BookPediaColors.SecondaryDark.copy(alpha = alpha.toFloat()),
            radius = 4.dp.toPx(),
            center = position
        )
    }
}