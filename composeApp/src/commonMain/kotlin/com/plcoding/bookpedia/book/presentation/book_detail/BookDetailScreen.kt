@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookEdition
import com.plcoding.bookpedia.book.domain.BookRatings
import com.plcoding.bookpedia.book.domain.BookShelves
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.core.presentation.SandYellow
import kotlin.math.min
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    selectedBookViewModel: SelectedBookViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isInCart = selectedBookViewModel.cartItems.value.any { book -> book.id == state.book?.id }
    viewModel.onAction(BookDetailAction.OnCartStatusChange(isInCart))

    val onAddToCart :(Book) -> Unit = {
        viewModel.onAction(BookDetailAction.OnAddToCartClick(it))
        selectedBookViewModel.addBookToCart(it)
    }

    val onRemoveFromCart: (Book) -> Unit = {
        viewModel.onAction(BookDetailAction.OnRemoveFromCartClick(it))
        selectedBookViewModel.removeBookFromCart(it)
    }

    StunningBookDetailScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is BookDetailAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        },
        onAddToCart = onAddToCart,
        onRemoveFromCart = onRemoveFromCart,
        isBookInCart = state.isInCart
    )
}

@Composable
private fun StunningBookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
    onAddToCart: (Book) -> Unit,
    onRemoveFromCart: (Book) -> Unit,
    isBookInCart: Boolean
) {
    val scrollState = rememberLazyListState()
    val density = LocalDensity.current

    // Calculate parallax and blur effects based on scroll
    val scrollOffset by remember {
        derivedStateOf {
            scrollState.firstVisibleItemScrollOffset.toFloat()
        }
    }

    val parallaxOffset = scrollOffset * 0.5f
    val blurRadius = min(scrollOffset / 100f, 8f)
    // Fix: Make headerAlpha more forgiving - only start fading after 200px scroll and slower fade
//    val headerAlpha = max(1f - max(0f, scrollOffset - 200f) / 600f, 0.3f)

    // Dynamic color extraction (simulated - in real app, extract from image)
    val dominantColor = Color(0xFF2E4057)
    val accentColor = Color(0xFF48CAE4)

    Box(modifier = Modifier.fillMaxSize()) {
        // Stunning gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = 0.9f),
                            dominantColor.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        state.book?.let { book ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Hero Section with Parallax
                item {
                    HeroSection(
                        book = book,
                        parallaxOffset = parallaxOffset,
                        headerAlpha = null,
                        blurRadius = blurRadius,
                        dominantColor = dominantColor,
                        accentColor = accentColor,
                        onAddToCart = onAddToCart,
                        onRemoveFromCart = onRemoveFromCart,
                        isBookInCart = isBookInCart
                    )
                }

                // Quick Info Chips
                item {
                    QuickInfoSection(book = book)
                }

                // Enhanced Details
                if (!state.isLoadingDetails) {
                    state.bookDetails?.let { details ->
                        item {
                            RatingsSectionStunning(details.ratings)
                        }

                        item {
                            ShelfsSectionStunning(details.shelves)
                        }

                        if (details.editions.isNotEmpty()) {
                            item {
                                EditionsSectionStunning(details.editions)
                            }
                        }
                    }
                }

                // Synopsis with beautiful typography
                if (!book.description.isNullOrBlank()) {
                    item {
                        SynopsisSectionStunning(book.description)
                    }
                }

                // Subjects as beautiful floating chips
                if (book.subjects.isNotEmpty()) {
                    item {
                        SubjectsSectionStunning(book.subjects)
                    }
                }

                // Excerpts with elegant cards
                if (book.excerpts.isNotEmpty()) {
                    item {
                        ExcerptsSectionStunning(book.excerpts)
                    }
                }
            }
        }

        // Loading state with beautiful animation
        if (state.isLoading && state.book == null) {
            LoadingOverlay()
        }

        // Floating action button for favorites
        state.book?.let {
            FloatingFavoriteButton(
                isFavorite = state.isFavorite,
                onClick = { onAction(BookDetailAction.OnFavoriteClick) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            )
        }

        // Error message
        // Stunning glassmorphic back button
        StunningBackButton(
            onClick = { onAction(BookDetailAction.OnBackClick) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        )

        state.errorMessage?.let { error ->
            ErrorMessage(error)
        }
    }
}

@Composable
private fun HeroSection(
    book: Book,
    parallaxOffset: Float,
    headerAlpha: Float?,
    blurRadius: Float,
    dominantColor: Color,
    accentColor: Color,
    onAddToCart: (Book) -> Unit,
    onRemoveFromCart: (Book) -> Unit,
    isBookInCart: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        // Background image with parallax and blur
        AsyncImage(
            model = book.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = with(LocalDensity.current) { parallaxOffset.toDp() })
                .blur(
                    radiusX = blurRadius.dp,
                    radiusY = blurRadius.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .alpha(0.7f),
            onError = { error ->
                println("Background image loading error for ${book.imageUrl}: ${error.result.throwable}")
            }
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            dominantColor.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Content - Remove headerAlpha dependency to prevent disappearing
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 3D Book Cover - Remove alpha modifier
            StunningBookCover(
                imageUrl = book.imageUrl,
                title = book.title
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title with stunning typography - Remove alpha modifier
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                ),
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .graphicsLayer {
                        shadowElevation = 8.dp.toPx()
                    }
            )

            book.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Author with accent color - Remove alpha modifier
            Text(
                text = "by ${book.authors.joinToString()}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                color = accentColor,
                modifier = Modifier.padding(top = 12.dp)
            )

            // Price display
            Text(
                text = "$${book.generateBookPrice()}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Cart button
            Spacer(modifier = Modifier.height(20.dp))
            CartButton(
                book = book,
                isInCart = isBookInCart,
                onAddToCart = onAddToCart,
                onRemoveFromCart = onRemoveFromCart
            )
        }
    }
}

@Composable
private fun StunningBookCover(
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier.scale(scale)
    ) {
        // Shadow layers for 3D effect
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(width = 180.dp, height = 260.dp)
                    .offset(x = (index * 2).dp, y = (index * 2).dp)
                    .background(
                        Color.Black.copy(alpha = 0.1f - index * 0.03f),
                        RoundedCornerShape(16.dp)
                    )
            )
        }

        // Main book cover
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 260.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun QuickInfoSection(book: com.plcoding.bookpedia.book.domain.Book) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (book.hasFulltext) {
            item {
                GlassmorphicChip(
                    icon = Icons.Default.MenuBook,
                    text = "Read Online",
                    color = Color(0xFF4CAF50)
                )
            }
        }

        book.availabilityStatus?.let { status ->
            item {
                GlassmorphicChip(
                    text = status.uppercase(),
                    color = Color(0xFF2196F3)
                )
            }
        }

        book.averageRating?.let { rating ->
            item {
                GlassmorphicChip(
                    icon = Icons.Default.Star,
                    text = "${round(rating * 10) / 10.0}",
                    color = SandYellow
                )
            }
        }

        book.numPages?.let { pages ->
            item {
                GlassmorphicChip(
                    text = "$pages pages",
                    color = Color(0xFF9C27B0)
                )
            }
        }

        if (book.numEditions > 0) {
            item {
                GlassmorphicChip(
                    text = "${book.numEditions} editions",
                    color = Color(0xFF795548)
                )
            }
        }
    }
}

@Composable
private fun GlassmorphicChip(
    text: String,
    icon: ImageVector? = null,
    color: Color
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        color.copy(alpha = 0.2f),
                        color.copy(alpha = 0.1f)
                    )
                )
            ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = color
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = color
            )
        }
    }
}

@Composable
private fun RatingsSectionStunning(ratings: BookRatings?) {
    ratings?.let { rating ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Reader Reviews",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                rating.average?.let { avg ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Large rating display
                        Text(
                            text = "${round(avg * 10) / 10.0}",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = SandYellow
                        )

                        Column {
                            // Star rating
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = if (index < avg.toInt()) SandYellow else Color.Gray.copy(alpha = 0.3f)
                                    )
                                }
                            }

                            rating.count?.let { count ->
                                Text(
                                    text = "${formatCount(count)} reviews",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Rating distribution with animated bars
                rating.distribution?.let { dist ->
                    Spacer(modifier = Modifier.height(20.dp))
                    val total = (dist.oneStar + dist.twoStars + dist.threeStars + dist.fourStars + dist.fiveStars).toFloat()

                    if (total > 0) {
                        listOf(5, 4, 3, 2, 1).forEachIndexed { index, stars ->
                            val count = when(stars) {
                                5 -> dist.fiveStars
                                4 -> dist.fourStars
                                3 -> dist.threeStars
                                2 -> dist.twoStars
                                else -> dist.oneStar
                            }

                            AnimatedRatingBar(
                                stars = stars,
                                count = count,
                                total = total.toInt(),
                                progress = count.toFloat() / total
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedRatingBar(
    stars: Int,
    count: Int,
    total: Int,
    progress: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$stars",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(16.dp)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = SandYellow
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(SandYellow, SandYellow.copy(alpha = 0.7f))
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = formatCount(count),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ShelfsSectionStunning(shelves: BookShelves?) {
    shelves?.let { shelf ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Reader Activity",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StunningShelfItem(
                        label = "Want to Read",
                        count = shelf.wantToRead,
                        icon = Icons.Default.BookmarkAdd,
                        color = Color(0xFF2196F3)
                    )

                    StunningShelfItem(
                        label = "Reading",
                        count = shelf.currentlyReading,
                        icon = Icons.Default.AutoStories,
                        color = Color(0xFF4CAF50)
                    )

                    StunningShelfItem(
                        label = "Read",
                        count = shelf.alreadyRead,
                        icon = Icons.Default.BookmarkAdded,
                        color = Color(0xFF9C27B0)
                    )
                }
            }
        }
    }
}

@Composable
private fun StunningShelfItem(
    label: String,
    count: Int,
    icon: ImageVector,
    color: Color
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = formatCount(count),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
private fun EditionsSectionStunning(editions: List<BookEdition>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Available Editions",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(editions.take(5)) { edition ->
                    StunningEditionCard(edition)
                }
            }
        }
    }
}

@Composable
private fun StunningEditionCard(edition: BookEdition) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            edition.coverIds.firstOrNull()?.let { coverId ->
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/$coverId-S.jpg",
                    contentDescription = edition.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = edition.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            edition.publishDate?.let { date ->
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SynopsisSectionStunning(description: String) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val canBeTruncated = description.length > 300

    val textToShow = if (isExpanded || !canBeTruncated) {
        description
    } else {
        "${description.take(300)}..."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = textToShow,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            if (canBeTruncated) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isExpanded) "Show Less" else "Show More",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun SubjectsSectionStunning(subjects: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.take(12).forEach { subject ->
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = subject,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExcerptsSectionStunning(excerpts: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Excerpts",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            excerpts.take(3).forEach { excerpt ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.05f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = "\"$excerpt\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun FloatingFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.primary,
            modifier = Modifier.shadow(8.dp, CircleShape)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun StunningBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.1f)
                    )
                )
            ),
        color = Color.Transparent
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun CartButton(
    book: Book,
    isInCart: Boolean,
    onAddToCart: (Book) -> Unit,
    onRemoveFromCart: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = if (isInCart) Color(0xFFE53E3E) else Color(0xFF38A169)
    val buttonText = if (isInCart) "Remove from Cart" else "Add to Cart"
    val buttonIcon = if (isInCart) Icons.Default.ShoppingCartCheckout else Icons.Default.AddShoppingCart

    val scale by animateFloatAsState(
        targetValue = if (isInCart) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically() + fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = modifier
                .scale(scale)
                .fillMaxWidth(0.8f)
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .clickable {
                    if (isInCart) {
                        onRemoveFromCart(book)
                    } else {
                        onAddToCart(book)
                    }
                }
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            buttonColor,
                            buttonColor.copy(alpha = 0.8f)
                        )
                    )
                ),
            color = Color.Transparent,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = buttonIcon,
                    contentDescription = buttonText,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${(count / 100_000) / 10.0}M"
        count >= 1_000 -> "${(count / 100) / 10.0}K"
        else -> count.toString()
    }
}
