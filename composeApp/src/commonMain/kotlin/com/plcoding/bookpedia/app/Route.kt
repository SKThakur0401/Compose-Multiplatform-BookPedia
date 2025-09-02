package com.plcoding.bookpedia.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object BookGraph: Route

    @Serializable
    data object SplashScreen: Route

    @Serializable
    data object BookList: Route

    @Serializable
    data class BookDetail(val id: String): Route

    @Serializable
    data object AddToCart: Route

    @Serializable
    data object CheckoutScreen: Route
}