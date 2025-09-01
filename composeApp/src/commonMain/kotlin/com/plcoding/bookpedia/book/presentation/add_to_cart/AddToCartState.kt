package com.plcoding.bookpedia.book.presentation.add_to_cart

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText


data class AddToCartState(
    val cartItems: List<Book> = emptyList(),
    val totalPrice: Double = 0.0,
    val totalItems: Int = cartItems.size,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isCheckingOut: Boolean = false
)
