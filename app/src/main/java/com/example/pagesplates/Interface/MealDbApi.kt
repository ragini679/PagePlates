package com.example.pagesplates.Interface

import com.example.pagesplates.Data.MealResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApi {
    @GET("search.php")
    fun searchMeals(
        @Query("s") searchTerm: String,
        @Query("page") page: Int
    ): Single<MealResponse>
}