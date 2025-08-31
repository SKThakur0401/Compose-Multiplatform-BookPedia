package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditionsResponseDto(
    @SerialName("links") val links: LinksDto? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("entries") val entries: List<EditionDto>? = null
)

@Serializable
data class LinksDto(
    @SerialName("self") val self: String? = null,
    @SerialName("work") val work: String? = null
)

@Serializable
data class EditionDto(
    @SerialName("key") val key: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("identifiers") val identifiers: IdentifiersDto? = null,
    @SerialName("publishers") val publishers: List<String>? = null,
    @SerialName("publish_date") val publishDate: String? = null,
    @SerialName("number_of_pages") val numberOfPages: Int? = null,
    @SerialName("physical_format") val physicalFormat: String? = null,
    @SerialName("covers") val covers: List<Int>? = null,
    @SerialName("languages") val languages: List<LanguageDto>? = null,
    @SerialName("isbn_10") val isbn10: List<String>? = null,
    @SerialName("isbn_13") val isbn13: List<String>? = null,
    @SerialName("oclc_numbers") val oclcNumbers: List<String>? = null,
    @SerialName("lccn") val lccn: List<String>? = null,
    @SerialName("dewey_decimal_class") val deweyDecimalClass: List<String>? = null,
    @SerialName("lc_classifications") val lcClassifications: List<String>? = null,
    @SerialName("source_records") val sourceRecords: List<String>? = null,
    @SerialName("full_title") val fullTitle: String? = null,
    @SerialName("works") val works: List<WorkRefDto>? = null,
    @SerialName("type") val type: TypeDto? = null,
    @SerialName("revision") val revision: Int? = null,
    @SerialName("created") val created: CreatedDto? = null,
    @SerialName("last_modified") val lastModified: CreatedDto? = null
)

@Serializable
data class IdentifiersDto(
    @SerialName("isbn_10") val isbn10: List<String>? = null,
    @SerialName("isbn_13") val isbn13: List<String>? = null,
    @SerialName("oclc") val oclc: List<String>? = null,
    @SerialName("lccn") val lccn: List<String>? = null,
    @SerialName("goodreads") val goodreads: List<String>? = null,
    @SerialName("librarything") val librarything: List<String>? = null
)

@Serializable
data class LanguageDto(
    @SerialName("key") val key: String? = null
)

@Serializable
data class WorkRefDto(
    @SerialName("key") val key: String? = null
)

@Serializable
data class CreatedDto(
    @SerialName("type") val type: String? = null,
    @SerialName("value") val value: String? = null
)

