package com.plcoding.bookpedia.book.presentation.add_to_cart

import com.plcoding.bookpedia.book.domain.Book

sealed interface AddToCartAction {
    data class UpdateCart(val cartList : List<Book>): AddToCartAction
}
