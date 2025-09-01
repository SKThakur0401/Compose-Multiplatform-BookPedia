package com.plcoding.bookpedia.book.presentation

import androidx.lifecycle.ViewModel
import com.plcoding.bookpedia.book.domain.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectedBookViewModel: ViewModel() {

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook = _selectedBook.asStateFlow()

    fun onSelectBook(book: Book?) {
        _selectedBook.value = book
    }

    private val _cartItems = MutableStateFlow<List<Book>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    fun addBookToCart(book: Book) {
        // Implementation for adding the book to the cart
        _cartItems.value += book
    }

    fun removeBookFromCart(book: Book) {
        // Implementation for removing the book from the cart
        _cartItems.value = _cartItems.value.filter { it.id != book.id }
    }
}