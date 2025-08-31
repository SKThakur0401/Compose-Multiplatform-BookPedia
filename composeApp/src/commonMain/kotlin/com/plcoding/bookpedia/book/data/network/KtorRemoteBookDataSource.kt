package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.BookshelvesResponseDto
import com.plcoding.bookpedia.book.data.dto.EditionsResponseDto
import com.plcoding.bookpedia.book.data.dto.RatingsResponseDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
): RemoteBookDataSource {

    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.Remote> {
        return safeCall<SearchResponseDto> {
            httpClient.get(
                urlString = "$BASE_URL/search.json"
            ) {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter("fields", "key,title,subtitle,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count,subject_facet,has_fulltext,ia,public_scan_b,availability,person,place,time,editions,editions.key,editions.title,editions.language,editions.ebook_access")
            }
        }
    }

    override suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote> {
        return safeCall<BookWorkDto> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookWorkId.json"
            )
        }
    }

    override suspend fun getBookEditions(bookWorkId: String): Result<EditionsResponseDto, DataError.Remote> {
        return safeCall<EditionsResponseDto> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookWorkId/editions.json"
            )
        }
    }

    override suspend fun getBookRatings(bookWorkId: String): Result<RatingsResponseDto, DataError.Remote> {
        return safeCall<RatingsResponseDto> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookWorkId/ratings.json"
            )
        }
    }

    override suspend fun getBookBookshelves(bookWorkId: String): Result<BookshelvesResponseDto, DataError.Remote> {
        return safeCall<BookshelvesResponseDto> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookWorkId/bookshelves.json"
            )
        }
    }
}