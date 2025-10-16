package com.plcoding.bookpedia.book.presentation.add_to_cart.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.components.ButtonSize
import com.plcoding.bookpedia.core.presentation.components.ButtonStyle
import com.plcoding.bookpedia.core.presentation.components.PremiumButton
import com.plcoding.bookpedia.core.presentation.components.RatingBar
import com.plcoding.bookpedia.core.presentation.theme.BookPediaAnimations
import com.plcoding.bookpedia.core.presentation.theme.BookPediaCustomShapes
import com.plcoding.bookpedia.core.presentation.theme.BookPediaCustomTypography
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EnhancedCartItemCard(
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