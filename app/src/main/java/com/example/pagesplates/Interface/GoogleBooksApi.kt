package com.example.pagesplates.Interface


import com.example.pagesplates.Data.BooksResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    fun searchBooks(
        @Query("q") searchTerm: String,
        @Query("startIndex") startIndex: Int
    ): Single<BooksResponse>
}
