package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.BookshelvesResponseDto
import com.plcoding.bookpedia.book.data.dto.EditionsResponseDto
import com.plcoding.bookpedia.book.data.dto.RatingsResponseDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>

    suspend fun getBookEditions(bookWorkId: String): Result<EditionsResponseDto, DataError.Remote>

    suspend fun getBookRatings(bookWorkId: String): Result<RatingsResponseDto, DataError.Remote>

    suspend fun getBookBookshelves(bookWorkId: String): Result<BookshelvesResponseDto, DataError.Remote>
}