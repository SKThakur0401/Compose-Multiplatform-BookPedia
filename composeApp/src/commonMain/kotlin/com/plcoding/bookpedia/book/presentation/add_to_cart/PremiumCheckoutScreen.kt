package com.plcoding.bookpedia.book.presentation.add_to_cart

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.add_to_cart.components.checkout_steps.OrderSummaryStep
import com.plcoding.bookpedia.book.presentation.add_to_cart.components.checkout_steps.PaymentDetailsStep
import com.plcoding.bookpedia.core.data.Utils
import com.plcoding.bookpedia.core.data.Utils.formatPrice
import com.plcoding.bookpedia.core.presentation.components.*
import com.plcoding.bookpedia.core.presentation.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PremiumCheckoutScreen(
    selectedBookViewModel: SelectedBookViewModel,
    onBackClick: () -> Unit = {}
) {
    val cartItems by selectedBookViewModel.cartItems.collectAsStateWithLifecycle()
    var checkoutStep by remember { mutableStateOf(CheckoutStep.OrderSummary) }
    var isProcessing by remember { mutableStateOf(false) }
    var orderCompleted by remember { mutableStateOf(false) }
    
    // Calculate pricing
    val subtotal = remember(cartItems) {
        cartItems.sumOf { it.generateBookPrice() }
    }
    
    val gstRate = 0.18
    val gstAmount = subtotal * gstRate
    val shippingCost = if (subtotal >= 50.0) 0.0 else 5.99
    val totalAmount = subtotal + gstAmount + shippingCost

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BookPediaColors.GradientStart.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Premium Header
            PremiumCheckoutHeader(
                currentStep = checkoutStep,
                onBackClick = onBackClick,
                orderCompleted = orderCompleted
            )
            
            // Progress Indicator
            CheckoutProgressIndicator(
                currentStep = checkoutStep,
                orderCompleted = orderCompleted,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
            
            // Main Content
            AnimatedContent(
                targetState = if (orderCompleted) CheckoutStep.Completed else checkoutStep,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState.ordinal > initialState.ordinal) it else -it },
                        animationSpec = BookPediaAnimations.emphasizedTweenIntOffset
                    ) with slideOutHorizontally(
                        targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -it else it },
                        animationSpec = BookPediaAnimations.emphasizedTweenIntOffset
                    )
                },
                label = "checkout_steps"
            ) { step ->
                when (step) {
                    CheckoutStep.OrderSummary -> OrderSummaryStep(
                        cartItems = cartItems,
                        subtotal = subtotal,
                        gstAmount = gstAmount,
                        shippingCost = shippingCost,
                        totalAmount = totalAmount,
                        onContinue = { checkoutStep = CheckoutStep.PaymentDetails }
                    )
                    CheckoutStep.PaymentDetails -> PaymentDetailsStep(
                        totalAmount = totalAmount,
                        onContinue = { 
                            checkoutStep = CheckoutStep.Processing
                            isProcessing = true
                        },
                        onBack = { checkoutStep = CheckoutStep.OrderSummary }
                    )
                    CheckoutStep.Processing -> ProcessingStep(
                        onComplete = {
                            isProcessing = false
                            orderCompleted = true
                        }
                    )
                    CheckoutStep.Completed -> CompletedStep(
                        totalAmount = totalAmount,
                        orderNumber = "184928", /*BP${System.currentTimeMillis().toString().takeLast(6)}*/
                        onContinueShopping = { onBackClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumCheckoutHeader(
    currentStep: CheckoutStep,
    onBackClick: () -> Unit,
    orderCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = if (currentStep == CheckoutStep.Processing || orderCompleted) 0.dp else 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!orderCompleted) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when {
                        orderCompleted -> "Order Confirmed!"
                        currentStep == CheckoutStep.Processing -> "Processing Order..."
                        else -> "Checkout"
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (!orderCompleted) {
                    Text(
                        text = when (currentStep) {
                            CheckoutStep.OrderSummary -> "Review your order"
                            CheckoutStep.PaymentDetails -> "Payment information"
                            CheckoutStep.Processing -> "Please wait..."
                            CheckoutStep.Completed -> "Thank you!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (orderCompleted) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            BookPediaColors.Success,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckoutProgressIndicator(
    currentStep: CheckoutStep,
    orderCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val steps = listOf("Summary", "Payment", "Complete")
    val currentStepIndex = if (orderCompleted) steps.size - 1 else minOf(currentStep.ordinal, steps.size - 1)
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Step Circle
                val isActive = index <= currentStepIndex
                val isCompleted = index < currentStepIndex || orderCompleted
                
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            when {
                                isCompleted -> BookPediaColors.Success
                                isActive -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Step Name
                Text(
                    text = stepName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
                    ),
                    color = if (isActive) MaterialTheme.colorScheme.onSurface 
                           else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Connector Line
            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(
                            if (index < currentStepIndex || orderCompleted) 
                                BookPediaColors.Success 
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}


@Composable
fun OrderItemSummary(book: Book) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Book Cover
        Card(
            modifier = Modifier.size(width = 40.dp, height = 60.dp),
            shape = BookPediaCustomShapes.BookCover
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Book Details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (book.authors.isNotEmpty()) {
                Text(
                    text = "by ${book.authors.first()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Price
        Text(
            text = "$${book.generateBookPrice()}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PriceBreakdownCard(
    subtotal: Double,
    gstAmount: Double,
    shippingCost: Double,
    totalAmount: Double
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = BookPediaCustomShapes.BookCard
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Price Breakdown",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            PriceRow("Subtotal", subtotal)
            PriceRow("Tax (18%)", gstAmount)
            PriceRow("Shipping", shippingCost, showFree = shippingCost == 0.0)
            
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = Utils.formatPrice(totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    amount: Double,
    showFree: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = if (showFree) "FREE" else formatPrice(amount),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (showFree) BookPediaColors.Success else MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun ProcessingStep(
    onComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000) // Simulate processing
        onComplete()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Processing Your Order",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Please wait while we process your payment...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CompletedStep(
    totalAmount: Double,
    orderNumber: String,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Animation Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    BookPediaColors.Success.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = BookPediaColors.Success,
                modifier = Modifier.size(64.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Order Confirmed!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Your order #$orderNumber has been placed successfully.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Total: ${formatPrice(totalAmount)}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        PremiumButton(
            text = "Continue Shopping",
            onClick = onContinueShopping,
            style = ButtonStyle.Primary,
            size = ButtonSize.Large,
            icon = Icons.Default.ArrowForward,
            iconPosition = IconPosition.Trailing,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


enum class CheckoutStep {
    OrderSummary,
    PaymentDetails,
    Processing,
    Completed
}
