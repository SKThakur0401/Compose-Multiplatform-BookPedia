package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingsResponseDto(
    @SerialName("summary") val summary: RatingSummaryDto? = null,
    @SerialName("counts") val counts: RatingCountsDto? = null
)

@Serializable
data class RatingSummaryDto(
    @SerialName("average") val average: Double? = null,
    @SerialName("count") val count: Int? = null,
    @SerialName("sortable") val sortable: Double? = null
)

@Serializable
data class RatingCountsDto(
    @SerialName("1") val oneStar: Int? = null,
    @SerialName("2") val twoStars: Int? = null,
    @SerialName("3") val threeStars: Int? = null,
    @SerialName("4") val fourStars: Int? = null,
    @SerialName("5") val fiveStars: Int? = null
)
