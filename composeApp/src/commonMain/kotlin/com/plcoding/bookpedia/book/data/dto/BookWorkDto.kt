package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.Serializable

// We need a "custom Serializer" to deserealize this bcoz API response is "String" type in some case
// whereas in some this API is returning a JSON object with "Value" and "type"... so there's
// no fixed data-type sent from backend :(
// "Value" contains the desired description in case of JSON
@Serializable(with = BookWorkDtoSerializer::class)
data class BookWorkDto(
    val description: String? = null
)
