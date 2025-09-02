package com.plcoding.bookpedia.book.presentation.add_to_cart.components

import androidx.compose.runtime.Composable
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.add_to_cart.PremiumCheckoutScreen

@Composable
fun CheckoutScreen(selectedBookViewModel: SelectedBookViewModel) {
    // Use the new premium implementation
    PremiumCheckoutScreen(selectedBookViewModel = selectedBookViewModel)
}