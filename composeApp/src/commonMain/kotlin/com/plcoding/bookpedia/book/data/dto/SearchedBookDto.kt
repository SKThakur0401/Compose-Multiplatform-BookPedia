package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedBookDto(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("cover_i") val coverAlternativeKey: Int? = null,
    @SerialName("author_key") val authorKeys: List<String>? = null,
    @SerialName("author_name") val authorNames: List<String>? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("ratings_average") val ratingsAverage: Double? = null,
    @SerialName("ratings_count") val ratingsCount: Int? = null,
    @SerialName("number_of_pages_median") val numPagesMedian: Int? = null,
    @SerialName("edition_count") val numEditions: Int? = null,
    @SerialName("subject_facet") val subjectFacet: List<String>? = null,
    @SerialName("has_fulltext") val hasFulltext: Boolean? = null,
    @SerialName("ia") val internetArchive: List<String>? = null,
    @SerialName("public_scan_b") val publicScan: Boolean? = null,
    @SerialName("availability") val availability: AvailabilityDto? = null,
    @SerialName("person") val person: List<String>? = null,
    @SerialName("place") val place: List<String>? = null,
    @SerialName("time") val time: List<String>? = null,
    @SerialName("editions") val editions: EditionsInfoDto? = null
)

@Serializable
data class AvailabilityDto(
    @SerialName("status") val status: String? = null,
    @SerialName("available_to_browse") val availableToBrowse: Boolean? = null,
    @SerialName("available_to_borrow") val availableToBorrow: Boolean? = null,
    @SerialName("available_to_waitlist") val availableToWaitlist: Boolean? = null,
    @SerialName("is_printdisabled") val isPrintDisabled: Boolean? = null,
    @SerialName("is_readable") val isReadable: Boolean? = null,
    @SerialName("is_lendable") val isLendable: Boolean? = null,
    @SerialName("is_previewable") val isPreviewable: Boolean? = null,
    @SerialName("identifier") val identifier: String? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("oclc") val oclc: String? = null,
    @SerialName("openlibrary_work") val openlibraryWork: String? = null,
    @SerialName("openlibrary_edition") val openlibraryEdition: String? = null,
    @SerialName("last_loan_date") val lastLoanDate: String? = null,
    @SerialName("num_waitlist") val numWaitlist: String? = null,
    @SerialName("last_waitlist_date") val lastWaitlistDate: String? = null,
    @SerialName("is_restricted") val isRestricted: Boolean? = null,
    @SerialName("is_browseable") val isBrowseable: Boolean? = null,
    @SerialName("__src__") val source: String? = null
)

@Serializable
data class EditionsInfoDto(
    @SerialName("numFound") val numFound: Int? = null,
    @SerialName("start") val start: Int? = null,
    @SerialName("numFoundExact") val numFoundExact: Boolean? = null,
    @SerialName("docs") val docs: List<EditionDocDto>? = null
)

@Serializable
data class EditionDocDto(
    @SerialName("key") val key: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("language") val language: List<String>? = null,
    @SerialName("ebook_access") val ebookAccess: String? = null
)
