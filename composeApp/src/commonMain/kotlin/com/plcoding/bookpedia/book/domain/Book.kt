package com.plcoding.bookpedia.book.domain

data class Book(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val authors: List<String>,
    val description: String?,
    val languages: List<String>,
    val firstPublishYear: String?,
    val averageRating: Double?,
    val ratingCount: Int?,
    val numPages: Int?,
    val numEditions: Int,
    val subjects: List<String> = emptyList(),
    val subjectPeople: List<String> = emptyList(),
    val subjectPlaces: List<String> = emptyList(),
    val subjectTimes: List<String> = emptyList(),
    val hasFulltext: Boolean = false,
    val availabilityStatus: String? = null,
    val isReadable: Boolean = false,
    val isLendable: Boolean = false,
    val excerpts: List<String> = emptyList(),
    val links: List<BookLink> = emptyList()
){
    // function to generate book price using book id so that each time a particular book is called it returns the same price
    fun generateBookPrice(): Double {
        val price = id.hashCode().toLong() % 100 // Limit seed to a reasonable range
        return if(price <= 0) {
            (price + 100) / 10.0 // Ensure positive price
        } else {
            price.toDouble()
        }
    }
}

data class BookLink(
    val title: String,
    val url: String
)

data class BookEdition(
    val key: String,
    val title: String,
    val subtitle: String? = null,
    val isbn10: List<String> = emptyList(),
    val isbn13: List<String> = emptyList(),
    val publishers: List<String> = emptyList(),
    val publishDate: String? = null,
    val numberOfPages: Int? = null,
    val physicalFormat: String? = null,
    val coverIds: List<Int> = emptyList(),
    val languages: List<String> = emptyList()
)

data class BookRatings(
    val average: Double? = null,
    val count: Int? = null,
    val distribution: RatingDistribution? = null
)

data class RatingDistribution(
    val oneStar: Int = 0,
    val twoStars: Int = 0,
    val threeStars: Int = 0,
    val fourStars: Int = 0,
    val fiveStars: Int = 0
)

data class BookShelves(
    val wantToRead: Int = 0,
    val currentlyReading: Int = 0,
    val alreadyRead: Int = 0
)

data class BookDetails(
    val book: Book,
    val editions: List<BookEdition> = emptyList(),
    val ratings: BookRatings? = null,
    val shelves: BookShelves? = null
)
