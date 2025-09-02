package com.plcoding.bookpedia.core.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.bookpedia.core.presentation.theme.*

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Primary,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.Leading
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animation for press feedback
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = BookPediaAnimations.bouncySpring,
        label = "button_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = when {
            loading -> 0.dp.value
            isPressed && enabled -> 2.dp.value
            enabled -> when (style) {
                ButtonStyle.Primary -> 4.dp.value
                ButtonStyle.Secondary -> 2.dp.value
                ButtonStyle.Outlined -> 0.dp.value
                ButtonStyle.Text -> 0.dp.value
            }
            else -> 0.dp.value
        },
        animationSpec = BookPediaAnimations.smoothTween,
        label = "button_elevation"
    )
    
    val buttonColors = getButtonColors(style)
    val buttonSize = getButtonSize(size)
    
    Surface(
        modifier = modifier
            .height(buttonSize.height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = when (style) {
            ButtonStyle.Primary -> BookPediaCustomShapes.PrimaryButton
            ButtonStyle.Secondary -> BookPediaCustomShapes.SecondaryButton
            else -> BookPediaCustomShapes.SecondaryButton
        },
        color = buttonColors.containerColor(enabled),
        shadowElevation = elevation.dp,
        border = when (style) {
            ButtonStyle.Outlined -> BorderStroke(
                width = 1.dp,
                color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
            )
            else -> null
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (style == ButtonStyle.Primary) {
                        Modifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    BookPediaColors.GradientStart,
                                    BookPediaColors.GradientMiddle,
                                    BookPediaColors.GradientEnd
                                )
                            )
                        )
                    } else Modifier
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled && !loading
                ) {
                    onClick()
                }
                .padding(horizontal = buttonSize.horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(buttonSize.iconSize),
                    color = buttonColors.contentColor(enabled),
                    strokeWidth = 2.dp
                )
            } else {
                ButtonContent(
                    text = text,
                    icon = icon,
                    iconPosition = iconPosition,
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        fontSize = buttonSize.fontSize,
                        fontWeight = FontWeight.Medium
                    ),
                    contentColor = buttonColors.contentColor(enabled),
                    iconSize = buttonSize.iconSize
                )
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?,
    iconPosition: IconPosition,
    textStyle: androidx.compose.ui.text.TextStyle,
    contentColor: Color,
    iconSize: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null && iconPosition == IconPosition.Leading) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Text(
            text = text,
            style = textStyle,
            color = contentColor
        )
        
        if (icon != null && iconPosition == IconPosition.Trailing) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = BookPediaAnimations.bouncySpring,
        label = "fab_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 4.dp.value else 8.dp.value,
        animationSpec = BookPediaAnimations.smoothTween,
        label = "fab_elevation"
    )
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .size(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = BookPediaCustomShapes.FloatingButton,
        color = containerColor,
        shadowElevation = elevation.dp,
        interactionSource = interactionSource
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun BadgedFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    badgeCount: Int = 0,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    badgeColor: Color = MaterialTheme.colorScheme.error
) {
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = onClick,
            icon = icon,
            contentDescription = contentDescription,
            containerColor = containerColor,
            contentColor = contentColor
        )
        
        if (badgeCount > 0) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp),
                containerColor = badgeColor
            ) {
                Text(
                    text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

// Helper data classes and enums
private data class ButtonColors(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color
) {
    fun containerColor(enabled: Boolean): Color = 
        if (enabled) container else container.copy(alpha = 0.12f)
    
    fun contentColor(enabled: Boolean): Color = 
        if (enabled) onContainer else onContainer.copy(alpha = 0.38f)
}

private data class ButtonSizeData(
    val height: Dp,
    val horizontalPadding: Dp,
    val fontSize: androidx.compose.ui.unit.TextUnit,
    val iconSize: Dp
)

@Composable
private fun getButtonColors(style: ButtonStyle): ButtonColors {
    return when (style) {
        ButtonStyle.Primary -> ButtonColors(
            primary = MaterialTheme.colorScheme.primary,
            onPrimary = MaterialTheme.colorScheme.onPrimary,
            container = MaterialTheme.colorScheme.primary,
            onContainer = MaterialTheme.colorScheme.onPrimary
        )
        ButtonStyle.Secondary -> ButtonColors(
            primary = MaterialTheme.colorScheme.secondary,
            onPrimary = MaterialTheme.colorScheme.onSecondary,
            container = MaterialTheme.colorScheme.secondary,
            onContainer = MaterialTheme.colorScheme.onSecondary
        )
        ButtonStyle.Outlined -> ButtonColors(
            primary = Color.Transparent,
            onPrimary = MaterialTheme.colorScheme.primary,
            container = Color.Transparent,
            onContainer = MaterialTheme.colorScheme.primary
        )
        ButtonStyle.Text -> ButtonColors(
            primary = Color.Transparent,
            onPrimary = MaterialTheme.colorScheme.primary,
            container = Color.Transparent,
            onContainer = MaterialTheme.colorScheme.primary
        )
    }
}

private fun getButtonSize(size: ButtonSize): ButtonSizeData {
    return when (size) {
        ButtonSize.Small -> ButtonSizeData(
            height = 36.dp,
            horizontalPadding = 16.dp,
            fontSize = 12.sp,
            iconSize = 16.dp
        )
        ButtonSize.Medium -> ButtonSizeData(
            height = 44.dp,
            horizontalPadding = 20.dp,
            fontSize = 14.sp,
            iconSize = 18.dp
        )
        ButtonSize.Large -> ButtonSizeData(
            height = 52.dp,
            horizontalPadding = 24.dp,
            fontSize = 16.sp,
            iconSize = 20.dp
        )
    }
}

enum class ButtonStyle {
    Primary,
    Secondary,
    Outlined,
    Text
}

enum class ButtonSize {
    Small,
    Medium,
    Large
}

enum class IconPosition {
    Leading,
    Trailing
}
