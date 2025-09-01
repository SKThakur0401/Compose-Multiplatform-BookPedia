package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookDetails

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null,
    val bookDetails: BookDetails? = null,
    val isLoadingDetails: Boolean = false,
    val errorMessage: String? = null,
    val isInCart: Boolean = false
)
