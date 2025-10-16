package com.plcoding.bookpedia.book.presentation.add_to_cart.components.checkout_steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.add_to_cart.OrderItemSummary
import com.plcoding.bookpedia.book.presentation.add_to_cart.PriceBreakdownCard
import com.plcoding.bookpedia.core.presentation.components.ButtonSize
import com.plcoding.bookpedia.core.presentation.components.ButtonStyle
import com.plcoding.bookpedia.core.presentation.components.IconPosition
import com.plcoding.bookpedia.core.presentation.components.PremiumButton
import com.plcoding.bookpedia.core.presentation.theme.BookPediaCustomShapes


@Composable
fun OrderSummaryStep(
    cartItems: List<Book>,
    subtotal: Double,
    gstAmount: Double,
    shippingCost: Double,
    totalAmount: Double,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Order Items
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = BookPediaCustomShapes.BookCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Order Items (${cartItems.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                cartItems.forEach { book ->
                    OrderItemSummary(book = book)
                    if (book != cartItems.last()) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }

        // Price Breakdown
        PriceBreakdownCard(
            subtotal = subtotal,
            gstAmount = gstAmount,
            shippingCost = shippingCost,
            totalAmount = totalAmount
        )

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        PremiumButton(
            text = "Continue to Payment",
            onClick = onContinue,
            style = ButtonStyle.Primary,
            size = ButtonSize.Large,
            icon = Icons.Default.ArrowForward,
            iconPosition = IconPosition.Trailing,
            modifier = Modifier.fillMaxWidth()
        )
    }
}