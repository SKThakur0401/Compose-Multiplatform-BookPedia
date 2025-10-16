package com.plcoding.bookpedia.core.data

import kotlin.math.round

object Utils {

    fun formatPrice(amount: Double): String {
        val roundedAmount = round(amount * 100) / 100
        return "$${roundedAmount}"
    }
}