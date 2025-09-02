package com.plcoding.bookpedia.book.presentation.add_to_cart

import androidx.compose.runtime.Composable
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel

@Composable
fun AddToCartScreen(
    viewModel: AddToCartViewModel,
    selectedBookViewModel: SelectedBookViewModel,
    onBackClick: () -> Unit,
    onCheckoutClicked: () -> Unit
) {
    // Use the new premium implementation
    PremiumAddToCartScreen(
        viewModel = viewModel,
        selectedBookViewModel = selectedBookViewModel,
        onBackClick = onBackClick,
        onCheckoutClicked = onCheckoutClicked
    )
}