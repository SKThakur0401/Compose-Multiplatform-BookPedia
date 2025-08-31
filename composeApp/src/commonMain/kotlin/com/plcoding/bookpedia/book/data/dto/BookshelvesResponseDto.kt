package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookshelvesResponseDto(
    @SerialName("counts") val counts: ShelfCountsDto? = null
)

@Serializable
data class ShelfCountsDto(
    @SerialName("want-to-read") val wantToRead: Int? = null,
    @SerialName("currently-reading") val currentlyReading: Int? = null,
    @SerialName("already-read") val alreadyRead: Int? = null
)
