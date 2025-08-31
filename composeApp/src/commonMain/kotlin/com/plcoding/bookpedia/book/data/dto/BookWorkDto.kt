package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// We need a "custom Serializer" to deserealize this bcoz API response is "String" type in some case
// whereas in some this API is returning a JSON object with "Value" and "type"... so there's
// no fixed data-type sent from backend :(
// "Value" contains the desired description in case of JSON
@Serializable(with = BookWorkDtoSerializer::class)
data class BookWorkDto(
    val description: String? = null,
    val subjects: List<String>? = null,
    @SerialName("subject_people") val subjectPeople: List<String>? = null,
    @SerialName("subject_places") val subjectPlaces: List<String>? = null,
    @SerialName("subject_times") val subjectTimes: List<String>? = null,
    val excerpts: List<ExcerptDto>? = null,
    val links: List<LinkDto>? = null,
    val title: String? = null,
    val key: String? = null,
    val authors: List<AuthorDto>? = null,
    @SerialName("first_publish_date") val firstPublishDate: String? = null
)

@Serializable
data class ExcerptDto(
    val excerpt: String? = null,
    val comment: String? = null
)

@Serializable
data class LinkDto(
    val title: String? = null,
    val url: String? = null,
    val type: TypeDto? = null
)

@Serializable
data class TypeDto(
    val key: String? = null
)

@Serializable
data class AuthorDto(
    val author: AuthorRefDto? = null,
    val type: TypeDto? = null
)

@Serializable
data class AuthorRefDto(
    val key: String? = null
)
