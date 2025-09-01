package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavoriteBookDao
import com.plcoding.bookpedia.book.data.mappers.enhanceBook
import com.plcoding.bookpedia.book.data.mappers.enhanceWithEditions
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEditions
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.data.mappers.toBookRatings
import com.plcoding.bookpedia.book.data.mappers.toBookShelves
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookDetails
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if(localResult == null) {
            remoteBookDataSource
                .getBookDetails(bookId)
                .map { it.description }
        } else {
            Result.Success(localResult.description)
        }
    }

    override suspend fun getBookDetails(bookId: String): Result<BookDetails, DataError.Remote> {
        return coroutineScope {
            // Start all API calls in parallel for better performance
            val workDetailsDeferred = async { remoteBookDataSource.getBookDetails(bookId) }
            val editionsDeferred = async { remoteBookDataSource.getBookEditions(bookId) }
            val ratingsDeferred = async { remoteBookDataSource.getBookRatings(bookId) }
            val bookshelvesDeferred = async { remoteBookDataSource.getBookBookshelves(bookId) }

            // Get the work details first as it's required for the base book info
            when (val workResult = workDetailsDeferred.await()) {
                is Result.Success -> {
                    // Get a base book from local favorites or create a minimal one
                    val localBook = favoriteBookDao.getFavoriteBook(bookId)?.toBook()
                    val baseBook = localBook ?: Book(
                        id = bookId,
                        title = /*workResult.data.title ?:*/ "Unknown Title",
                        imageUrl = "",
                        authors = emptyList(),
                        description = null,
                        languages = emptyList(),
                        firstPublishYear = /*workResult.data.firstPublishDate ?:*/ null,
                        averageRating = null,
                        ratingCount = null,
                        numPages = null,
                        numEditions = 0
                    )

                    // Enhance the book with work details
                    val enhancedBook = workResult.data.enhanceBook(baseBook)

                    // Get additional data, handling failures gracefully
                    val editions = when (val editionsResult = editionsDeferred.await()) {
                        is Result.Success -> editionsResult.data.toBookEditions()
                        is Result.Error -> emptyList()
                    }

                    val ratings = when (val ratingsResult = ratingsDeferred.await()) {
                        is Result.Success -> ratingsResult.data.toBookRatings()
                        is Result.Error -> null
                    }

                    val shelves = when (val shelvesResult = bookshelvesDeferred.await()) {
                        is Result.Success -> shelvesResult.data.toBookShelves()
                        is Result.Error -> null
                    }

                    Result.Success(
                        BookDetails(
                            book = enhancedBook,
                            editions = editions,
                            ratings = ratings,
                            shelves = shelves
                        )
                    )
                }
                is Result.Error -> Result.Error(workResult.error)
            }
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch(e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }
}