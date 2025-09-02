package com.plcoding.bookpedia.book.presentation.add_to_cart

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.core.presentation.components.*
import com.plcoding.bookpedia.core.presentation.theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PremiumAddToCartScreen(
    viewModel: AddToCartViewModel,
    selectedBookViewModel: SelectedBookViewModel,
    onBackClick: () -> Unit,
    onCheckoutClicked: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cartItems by selectedBookViewModel.cartItems.collectAsStateWithLifecycle()
    viewModel.onAction(AddToCartAction.UpdateCart(cartItems))
    
    val onRemoveClick = { book: Book ->
        selectedBookViewModel.removeBookFromCart(book)
    }
    
    val totalPrice by remember(cartItems) {
        derivedStateOf {
            cartItems.sumOf { it.generateBookPrice() }
        }
    }
    
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BookPediaColors.GradientStart.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Enhanced Top Bar
            PremiumTopAppBar(
                title = "Shopping Cart",
                subtitle = "${cartItems.size} ${if (cartItems.size == 1) "item" else "items"}",
                onBackClick = onBackClick
            )
            
            if (cartItems.isEmpty()) {
                // Enhanced Empty Cart State
                EmptyState(
                    type = EmptyStateType.EmptyCart,
                    modifier = Modifier.weight(1f),
                    onActionClick = onBackClick
                )
            } else {
                // Cart Items List
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = cartItems,
                        key = { it.id }
                    ) { book ->
                        EnhancedCartItemCard(
                            book = book,
                            onRemoveClick = { onRemoveClick(book) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                    
                    // Add some bottom padding for the checkout bar
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
        
        // Enhanced Checkout Bar
        if (cartItems.isNotEmpty()) {
            PremiumCheckoutBar(
                totalPrice = totalPrice,
                itemCount = cartItems.size,
                onCheckoutClick = onCheckoutClicked,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun PremiumTopAppBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Back Button
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
            
            // Title Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Shopping cart icon
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun EnhancedCartItemCard(
    book: Book,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRemoving by remember { mutableStateOf(false) }
    
    AnimatedVisibility(
        visible = !isRemoving,
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = BookPediaAnimations.emphasizedTweenIntOffset
        ) + fadeOut(animationSpec = BookPediaAnimations.smoothTween),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = BookPediaCustomShapes.BookCard,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Enhanced Book Cover
                Card(
                    modifier = Modifier.size(width = 60.dp, height = 90.dp),
                    shape = BookPediaCustomShapes.BookCover,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = book.title,
                        style = BookPediaCustomTypography.BookTitle.copy(fontSize = 16.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (book.authors.isNotEmpty()) {
                        Text(
                            text = "by ${book.authors.joinToString()}",
                            style = BookPediaCustomTypography.AuthorName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Rating if available
                    book.averageRating?.let { rating ->
                        RatingBar(
                            rating = rating,
                            size = 12.dp,
                            spacing = 1.dp,
                            showRatingText = true,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    // Price
                    Text(
                        text = "$${book.generateBookPrice()}",
                        style = BookPediaCustomTypography.PriceText.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Remove Button
                PremiumButton(
                    text = "Remove",
                    onClick = {
                        isRemoving = true
                        // Delay the actual removal to allow animation to play
                        GlobalScope.launch {
                            delay(300)
                            onRemoveClick()
                        }
                    },
                    style = ButtonStyle.Outlined,
                    size = ButtonSize.Small,
                    icon = Icons.Default.Delete,
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }
}

@Composable
private fun PremiumCheckoutBar(
    totalPrice: Double,
    itemCount: Int,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animated entrance
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(itemCount) {
        isVisible = itemCount > 0
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = BookPediaAnimations.emphasizedTweenIntOffset
        ) + fadeIn(animationSpec = BookPediaAnimations.smoothTween),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = BookPediaAnimations.emphasizedTweenIntOffset
        ) + fadeOut(animationSpec = BookPediaAnimations.smoothTween),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp,
            shape = BookPediaCustomShapes.TopContainer
        ) {
            Column {
                // Subtle divider
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    thickness = 1.dp
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price Summary
                    Column {
                        Text(
                            text = "Total (${itemCount} ${if (itemCount == 1) "item" else "items"})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Text(
                            text = "$${totalPrice}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Checkout Button
                    PremiumButton(
                        text = "Proceed to Checkout",
                        onClick = onCheckoutClick,
                        style = ButtonStyle.Primary,
                        size = ButtonSize.Large,
                        icon = Icons.Default.ArrowForward,
                        iconPosition = IconPosition.Trailing
                    )
                }
            }
        }
    }
}
