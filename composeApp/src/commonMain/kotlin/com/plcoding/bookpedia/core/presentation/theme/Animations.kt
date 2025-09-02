package com.plcoding.bookpedia.core.presentation.theme

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.unit.IntOffset
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


// Premium animation specs for consistent and smooth motion
object BookPediaAnimations {
    
    // Duration constants
    const val DURATION_SHORT = 200
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500
    const val DURATION_EXTRA_LONG = 800
    
    // Standard easing curves
    val FastOutSlowInEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val LinearOutSlowInEasing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val FastOutLinearInEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
    val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    
    // Smooth tween specs
    val smoothTween = tween<Float>(
        durationMillis = DURATION_MEDIUM,
        easing = FastOutSlowInEasing
    )
    
    val quickTween = tween<Float>(
        durationMillis = DURATION_SHORT,
        easing = FastOutLinearInEasing
    )
    
    val emphasizedTween = tween<Float>(
        durationMillis = DURATION_LONG,
        easing = EmphasizedEasing
    )
    
    // IntOffset animation specs for slide animations
    val smoothTweenIntOffset = tween<IntOffset>(
        durationMillis = DURATION_MEDIUM,
        easing = FastOutSlowInEasing
    )

    val quickTweenIntOffset = tween<IntOffset>(
        durationMillis = DURATION_SHORT,
        easing = FastOutLinearInEasing
    )

    val emphasizedTweenIntOffset = tween<IntOffset>(
        durationMillis = DURATION_LONG,
        easing = EmphasizedEasing
    )

    // IntSize animation specs for size animations (expand/shrink)
    val smoothTweenIntSize = tween<IntSize>(
        durationMillis = DURATION_MEDIUM,
        easing = FastOutSlowInEasing
    )

    val quickTweenIntSize = tween<IntSize>(
        durationMillis = DURATION_SHORT,
        easing = FastOutLinearInEasing
    )

    val emphasizedTweenIntSize = tween<IntSize>(
        durationMillis = DURATION_LONG,
        easing = EmphasizedEasing
    )
    
    // Spring animations for interactive elements
    val bouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val smoothSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val lowBounceSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessHigh
    )
    
    // Predefined enter/exit transitions
    val slideInFromRight = slideInHorizontally(
        animationSpec = emphasizedTweenIntOffset,
        initialOffsetX = { it }
    )
    
    val slideOutToLeft = slideOutHorizontally(
        animationSpec = emphasizedTweenIntOffset,
        targetOffsetX = { -it }
    )
    
    val slideInFromBottom = slideInVertically(
        animationSpec = emphasizedTweenIntOffset,
        initialOffsetY = { it }
    )
    
    val slideOutToTop = slideOutVertically(
        animationSpec = emphasizedTweenIntOffset,
        targetOffsetY = { -it }
    )
    
    val fadeInScale = fadeIn(animationSpec = smoothTween) + scaleIn(
        animationSpec = smoothTween,
        initialScale = 0.8f
    )
    
    val fadeOutScale = fadeOut(animationSpec = smoothTween) + scaleOut(
        animationSpec = smoothTween,
        targetScale = 0.8f
    )
    
    // Navigation transitions
    val navigationEnter = slideInFromRight + fadeIn(animationSpec = smoothTween)
    val navigationExit = slideOutToLeft + fadeOut(animationSpec = smoothTween)
    
    val popEnter = slideInHorizontally(
        animationSpec = emphasizedTweenIntOffset,
        initialOffsetX = { -it / 3 }
    ) + fadeIn(animationSpec = smoothTween)

    private val slideOutToRight = slideOutHorizontally(
        animationSpec = emphasizedTweenIntOffset,
        targetOffsetX = { it }
    )
    
    val popExit = slideOutToRight + fadeOut(animationSpec = smoothTween)

}

// Bouncy animation composable for interactive elements
@Composable
fun BouncyClickable(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    bounceScale: Float = 0.95f,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) bounceScale else 1f,
        animationSpec = BookPediaAnimations.bouncySpring,
        label = "bounce_scale"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        content()
    }
}

// Shimmer animation for loading states
@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1200
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
            }
    )
}

// Pulse animation for loading indicators
@Composable
fun PulseAnimation(
    modifier: Modifier = Modifier,
    minScale: Float = 0.8f,
    maxScale: Float = 1.2f,
    duration: Int = BookPediaAnimations.DURATION_EXTRA_LONG
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    )
}

// Floating animation for FABs and floating elements
@Composable
fun FloatingAnimation(
    modifier: Modifier = Modifier,
    offsetRange: Dp = 4.dp,
    duration: Int = 3000
) {
    val density = LocalDensity.current
    val offsetRangePx = with(density) { offsetRange.toPx() }
    
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -offsetRangePx,
        targetValue = offsetRangePx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_offset"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY
            }
    )
}

// Staggered list animation
@Composable
fun StaggeredVisibility(
    visible: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
    staggerDelay: Int = 50,
    enter: EnterTransition = BookPediaAnimations.fadeInScale,
    exit: ExitTransition = BookPediaAnimations.fadeOutScale,
    content: @Composable () -> Unit
) {
    LaunchedEffect(visible) {
        if (visible) {
            kotlinx.coroutines.delay(index * staggerDelay.toLong())
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}

// Page curl animation for book-like transitions
@Composable
fun PageCurlAnimation(
    progress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val rotationY by animateFloatAsState(
        targetValue = progress * 180f,
        animationSpec = BookPediaAnimations.emphasizedTween,
        label = "page_curl"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 12f * density
            }
    ) {
        content()
    }
}

// Ripple-like expand animation
@Composable
fun ExpandingCircleAnimation(
    triggered: Boolean,
    modifier: Modifier = Modifier,
    maxScale: Float = 3f,
    duration: Int = BookPediaAnimations.DURATION_LONG,
    content: @Composable () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (triggered) maxScale else 1f,
        animationSpec = tween(durationMillis = duration, easing = BookPediaAnimations.EmphasizedEasing),
        label = "expanding_circle"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (triggered) 0f else 1f,
        animationSpec = tween(durationMillis = duration, easing = LinearEasing),
        label = "expanding_alpha"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
    ) {
        content()
    }
}
