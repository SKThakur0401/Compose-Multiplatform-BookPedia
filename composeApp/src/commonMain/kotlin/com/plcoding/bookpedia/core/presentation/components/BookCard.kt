package com.plcoding.bookpedia.core.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.book_error_2
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.theme.*
import org.jetbrains.compose.resources.painterResource
import kotlin.math.round

@Composable
fun BookCard(
    book: Book,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit = {},
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier,
    cardStyle: BookCardStyle = BookCardStyle.Standard
) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Animation for press feedback
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = BookPediaAnimations.bouncySpring,
        label = "card_press_scale"
    )
    
    // Hover elevation animation
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2.dp.value else 8.dp.value,
        animationSpec = BookPediaAnimations.smoothTween,
        label = "card_elevation"
    )
    
    // Favorite animation
    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "favorite_scale"
    )

    when (cardStyle) {
        BookCardStyle.Standard -> StandardBookCard(
            book = book,
            onBookClick = onBookClick,
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite,
            isPressed = isPressed,
            onPressChanged = { isPressed = it },
            scale = scale,
            elevation = elevation.dp,
            favoriteScale = favoriteScale,
            modifier = modifier
        )
        BookCardStyle.Featured -> FeaturedBookCard(
            book = book,
            onBookClick = onBookClick,
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite,
            isPressed = isPressed,
            onPressChanged = { isPressed = it },
            scale = scale,
            elevation = elevation.dp,
            favoriteScale = favoriteScale,
            modifier = modifier
        )
        BookCardStyle.Compact -> CompactBookCard(
            book = book,
            onBookClick = onBookClick,
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite,
            isPressed = isPressed,
            onPressChanged = { isPressed = it },
            scale = scale,
            elevation = elevation.dp,
            favoriteScale = favoriteScale,
            modifier = modifier
        )
    }
}

@Composable
private fun StandardBookCard(
    book: Book,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    isFavorite: Boolean,
    isPressed: Boolean,
    onPressChanged: (Boolean) -> Unit,
    scale: Float,
    elevation: Dp,
    favoriteScale: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(width = 160.dp, height = 280.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = elevation,
                shape = BookPediaCustomShapes.BookCard,
                ambientColor = BookPediaColors.PrimaryDark.copy(alpha = 0.1f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onBookClick(book)
            },
        shape = BookPediaCustomShapes.BookCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Book Cover
                BookCoverImage(
                    imageUrl = book.imageUrl,
                    title = book.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(BookPediaCustomShapes.BookCover)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Book Title
                Text(
                    text = book.title,
                    style = BookPediaCustomTypography.BookTitle.copy(fontSize = 14.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Author
                book.authors.firstOrNull()?.let { author ->
                    Text(
                        text = author,
                        style = BookPediaCustomTypography.AuthorName.copy(fontSize = 12.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Rating and Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    book.averageRating?.let { rating ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = BookPediaColors.RatingStarYellow,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "${round(rating * 10) / 10}",
                                style = BookPediaCustomTypography.RatingText.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Price
                    Text(
                        text = "$${book.generateBookPrice()}",
                        style = BookPediaCustomTypography.PriceText.copy(fontSize = 12.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Favorite Button
            IconButton(
                onClick = { onFavoriteClick(book) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .graphicsLayer {
                        scaleX = favoriteScale
                        scaleY = favoriteScale
                    }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (isFavorite) BookPediaColors.WishlistPink else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun FeaturedBookCard(
    book: Book,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    isFavorite: Boolean,
    isPressed: Boolean,
    onPressChanged: (Boolean) -> Unit,
    scale: Float,
    elevation: Dp,
    favoriteScale: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = elevation,
                shape = BookPediaCustomShapes.FeatureCard,
                ambientColor = BookPediaColors.PrimaryDark.copy(alpha = 0.1f)
            )
            .clickable { onBookClick(book) },
        shape = BookPediaCustomShapes.FeatureCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Book Cover
                BookCoverImage(
                    imageUrl = book.imageUrl,
                    title = book.title,
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .clip(BookPediaCustomShapes.BookCoverLarge)
                )
                
                // Book Details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = book.title,
                            style = BookPediaCustomTypography.BookTitle.copy(fontSize = 18.sp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        book.authors.firstOrNull()?.let { author ->
                            Text(
                                text = "by $author",
                                style = BookPediaCustomTypography.AuthorName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Description
                        book.description?.let { description ->
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    // Bottom Row with Rating and Price
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        book.averageRating?.let { rating ->
                            RatingBar(
                                rating = rating,
                                size = 16.dp,
                                modifier = Modifier
                            )
                        }
                        
                        Text(
                            text = "$${book.generateBookPrice()}",
                            style = BookPediaCustomTypography.PriceText.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Favorite Button
            IconButton(
                onClick = { onFavoriteClick(book) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .graphicsLayer {
                        scaleX = favoriteScale
                        scaleY = favoriteScale
                    }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (isFavorite) BookPediaColors.WishlistPink else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactBookCard(
    book: Book,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    isFavorite: Boolean,
    isPressed: Boolean,
    onPressChanged: (Boolean) -> Unit,
    scale: Float,
    elevation: Dp,
    favoriteScale: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { onBookClick(book) },
        shape = BookPediaCustomShapes.BookCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Cover
            BookCoverImage(
                imageUrl = book.imageUrl,
                title = book.title,
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .clip(BookPediaCustomShapes.BookCover)
            )
            
            // Book Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                book.authors.firstOrNull()?.let { author ->
                    Text(
                        text = author,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Price and Favorite
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onFavoriteClick(book) },
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer {
                            scaleX = favoriteScale
                            scaleY = favoriteScale
                        }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (isFavorite) BookPediaColors.WishlistPink else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Text(
                    text = "$${book.generateBookPrice()}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BookCoverImage(
    imageUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    var imageLoadResult by remember { mutableStateOf<Result<androidx.compose.ui.graphics.painter.Painter>?>(null) }
    
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            imageLoadResult = if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image size"))
            }
        },
        onError = {
            imageLoadResult = Result.failure(it.result.throwable)
        }
    )
    
    val painterState by painter.state.collectAsStateWithLifecycle()
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (val result = imageLoadResult) {
            null -> {
                // Loading shimmer
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = BookPediaCustomShapes.BookCover
                        )
                )
            }
            else -> {
                Image(
                    painter = if (result.isSuccess) painter else painterResource(Res.drawable.book_error_2),
                    contentDescription = title,
                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

enum class BookCardStyle {
    Standard,    // Grid card for book lists
    Featured,    // Horizontal card for featured books
    Compact      // Small horizontal card for search results
}
