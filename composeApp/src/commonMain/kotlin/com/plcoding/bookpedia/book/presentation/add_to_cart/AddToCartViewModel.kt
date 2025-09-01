package com.plcoding.bookpedia.book.presentation.add_to_cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddToCartViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddToCartState())
    val state = _state.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AddToCartState()
        )




    fun onAction(action: AddToCartAction) {
        when (action) {
            is AddToCartAction.UpdateCart -> {
                _state.update { it.copy(cartItems = action.cartList) }
            }
        }
    }

    /*private fun addBookToCart(book: Book) {
        _state.update { state ->
            val existingItem = state.cartItems.find { it.book.id == book.id }
            val updatedItems = if (existingItem != null) {
                state.cartItems.map { item ->
                    if (item.book.id == book.id) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
            } else {
                state.cartItems + CartItem(book = book, quantity = 1)
            }

            state.copy(
                cartItems = updatedItems,
                totalItems = updatedItems.sumOf { it.quantity },
                totalPrice = calculateTotalPrice(updatedItems)
            )
        }
    }*/

/*    private fun removeBookFromCart(book: Book) {
        _state.update { state ->
            val updatedItems = state.cartItems.filter { it.book.id != book.id }
            state.copy(
                cartItems = updatedItems,
                totalItems = updatedItems.sumOf { it.quantity },
                totalPrice = calculateTotalPrice(updatedItems)
            )
        }
    }*/

    /*private fun updateBookQuantity(book: Book, quantity: Int) {
        if (quantity <= 0) {
            removeBookFromCart(book)
            return
        }

        _state.update { state ->
            val updatedItems = state.cartItems.map { item ->
                if (item.book.id == book.id) {
                    item.copy(quantity = quantity)
                } else {
                    item
                }
            }

            state.copy(
                cartItems = updatedItems,
                totalItems = updatedItems.sumOf { it.quantity },
                totalPrice = calculateTotalPrice(updatedItems)
            )
        }
    }*/

    private fun clearCart() {
        _state.update { state ->
            state.copy(
                cartItems = emptyList(),
                totalItems = 0,
                totalPrice = 0.0
            )
        }
    }

/*    private fun checkout() {
        viewModelScope.launch {
            _state.update { it.copy(isCheckingOut = true, errorMessage = null) }

            try {
                // Simulate checkout process
                kotlinx.coroutines.delay(2000)

                // On successful checkout, clear the cart
                _state.update { state ->
                    state.copy(
                        cartItems = emptyList(),
                        totalItems = 0,
                        totalPrice = 0.0,
                        isCheckingOut = false
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        isCheckingOut = false,
                        errorMessage = e.toUiText()
                    )
                }
            }
        }
    }*/

    private fun calculateTotalPrice(cartItemsAndQuantity: List<Pair<Book, Int>> ) : Double{
        var summingPrice: Double = 0.0

        for(item in cartItemsAndQuantity){
            summingPrice += item.first.generateBookPrice() * item.second
        }
        return summingPrice
    }
}
