package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
    data class OnAddToCartClick(val book: Book): BookDetailAction
    data class OnRemoveFromCartClick(val book: Book): BookDetailAction
    data class OnCartStatusChange(val isInCart: Boolean): BookDetailAction
}