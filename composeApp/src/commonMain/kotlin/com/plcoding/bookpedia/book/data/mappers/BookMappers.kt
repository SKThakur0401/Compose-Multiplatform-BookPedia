package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

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
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
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