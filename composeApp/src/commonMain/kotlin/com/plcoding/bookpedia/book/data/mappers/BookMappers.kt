package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.BookshelvesResponseDto
import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.EditionDto
import com.plcoding.bookpedia.book.data.dto.EditionsResponseDto
import com.plcoding.bookpedia.book.data.dto.RatingsResponseDto
import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookDetails
import com.plcoding.bookpedia.book.domain.BookEdition
import com.plcoding.bookpedia.book.domain.BookLink
import com.plcoding.bookpedia.book.domain.BookRatings
import com.plcoding.bookpedia.book.domain.BookShelves
import com.plcoding.bookpedia.book.domain.RatingDistribution

// "Dto" -> Stands for data transfer object (When data is obtained from backend.. it's obtained in
// this data class... then from this data class... data is mapped to the original data class (Which
// will also be there in domain, that domain layer data class will be used in presentation layer
// here it is "Book" data class)

// This is to preserve the very essence of MVVM + Clean Architecture! changes will only be made in
// data layer when some api related changes have to be done... and domain & Presentation won't be
// affected... we'll change these DTO data classes & maybe mappers in accordance to backend changes
// This way domain layer will only be affected only when some serious logic level changes are
// being done


// Moreover data obtained from backend maybe used in a processed way on android... eg. in case of
// "SearchedBookDto" ... imageUrl is obtained from coverKey/coverAlternateKey sometimes.. (See code
// below)
// so that can also be done by using these 2 data classes...
// Similarly there's a 3rd data class.. "BookEntity" for storing data in RoomDB... room has it's
// own set of rules.. like "List<String>" cannot be put in directly... it requires "TypeConverters"
// etc to handle all such things its better to have a 3rd data class...

// So "Book" is the actual data class used in presentation layer ... and "SearchedBookDto" is
// data obtained from backend and "BookEntity" is data stored in Room DB !! All 3 are the same thing
// so they need to be interconvertible... Hence this file is made,
// It contains all the interconversion functions...



fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if(coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear?.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0,
        subjects = subjectFacet ?: emptyList(),
        subjectPeople = person ?: emptyList(),
        subjectPlaces = place ?: emptyList(),
        subjectTimes = time ?: emptyList(),
        hasFulltext = hasFulltext ?: false,
        availabilityStatus = availability?.status,
        isReadable = availability?.isReadable ?: false,
        isLendable = availability?.isLendable ?: false
    )
}

fun BookWorkDto.enhanceBook(existingBook: Book): Book {
    return existingBook.copy(
        description = description ?: existingBook.description,
        subjects = subjects ?: existingBook.subjects,
        subjectPeople = subjectPeople ?: existingBook.subjectPeople,
        subjectPlaces = subjectPlaces ?: existingBook.subjectPlaces,
        subjectTimes = subjectTimes ?: existingBook.subjectTimes,
        excerpts = excerpts?.mapNotNull { it.excerpt } ?: existingBook.excerpts,
        links = links?.mapNotNull { linkDto ->
            if (linkDto.title != null && linkDto.url != null) {
                BookLink(title = linkDto.title, url = linkDto.url)
            } else null
        } ?: existingBook.links
    )
}

// Add a new function to enhance book with edition data
fun Book.enhanceWithEditions(editions: List<BookEdition>): Book {
    // If the current book has a fallback image URL, try to get a better one from editions
    val betterImageUrl = if (imageUrl.contains("/id/0-L.jpg") && editions.isNotEmpty()) {
        // Find the first edition with a cover ID
        val firstEditionWithCover = editions.find { it.coverIds.isNotEmpty() }
        firstEditionWithCover?.let { edition ->
            val coverId = edition.coverIds.first()
            "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
        } ?: imageUrl
    } else {
        imageUrl
    }

    return this.copy(imageUrl = betterImageUrl)
}

fun EditionsResponseDto.toBookEditions(): List<BookEdition> {
    return entries?.mapNotNull { it.toBookEdition() } ?: emptyList()
}

fun EditionDto.toBookEdition(): BookEdition? {
    return if (title != null) {
        BookEdition(
            key = key ?: "",
            title = title,
            publishers = publishers ?: emptyList(),
            publishDate = publishDate,
            numberOfPages = numberOfPages,
            coverIds = covers ?: emptyList(),
            languages = languages?.mapNotNull { it.key?.substringAfterLast("/") } ?: emptyList()
        )
    } else null
}

fun RatingsResponseDto.toBookRatings(): BookRatings {
    return BookRatings(
        average = summary?.average,
        count = summary?.count,
        distribution = counts?.let {
            RatingDistribution(
                oneStar = it.oneStar ?: 0,
                twoStars = it.twoStars ?: 0,
                threeStars = it.threeStars ?: 0,
                fourStars = it.fourStars ?: 0,
                fiveStars = it.fiveStars ?: 0
            )
        }
    )
}

fun BookshelvesResponseDto.toBookShelves(): BookShelves {
    return BookShelves(
        wantToRead = counts?.wantToRead ?: 0,
        currentlyReading = counts?.currentlyReading ?: 0,
        alreadyRead = counts?.alreadyRead ?: 0
    )
}

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        languages = languages,
        authors = authors,
        firstPublishYear = firstPublishYear,
        ratingsAverage = averageRating,
        ratingsCount = ratingCount,
        numPagesMedian = numPages,
        numEditions = numEditions
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        languages = languages,
        authors = authors,
        firstPublishYear = firstPublishYear,
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions
    )
}