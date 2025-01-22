package com.example.pagesplates.Object

import android.app.Application
import android.util.Log
import com.example.pagesplates.Data.BooksResponse
import com.example.pagesplates.Data.MealResponse
import com.example.pagesplates.Interface.GoogleBooksApi
import com.example.pagesplates.Interface.MealDbApi
import io.reactivex.rxjava3.core.Single
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object ApiService {
    private const val GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/"
    private const val MEAL_DB_BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    private fun provideCache(application: Application):Cache{
        val cacheDir=File(application.cacheDir,"http_cache")
        val cacheSize=(10*1024*1024).toLong()
        return Cache(cacheDir, cacheSize)
    }
    private fun provideOkHttpClient(application: Application): OkHttpClient {
        val cacheInterceptor = Interceptor { chain ->
            val request = chain.request()
            val cachedRequest = request.newBuilder()
                .header("Cache-Control", "public, max-age=60")
                .build()
            val response = chain.proceed(cachedRequest)
            if (response.cacheResponse != null) {
                // Log when the response is served from the cache
                Log.d("Cache", "Found response in cache: ${request.url}")
            } else {
                // Log when the response is served from the network
                Log.d("Cache", "Response served from network: ${request.url}")
            }
            return@Interceptor response
        }
        return OkHttpClient.Builder()
            .cache(provideCache(application))
            .addNetworkInterceptor(cacheInterceptor)
            .build()
    }
    fun provideGoogleBooksApi(application: Application): GoogleBooksApi {
        val client = provideOkHttpClient(application)
        return Retrofit.Builder()
            .baseUrl(GOOGLE_BOOKS_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }

    fun provideMealDbApi(application:Application): MealDbApi {
        val client = provideOkHttpClient(application)
        return Retrofit.Builder()
            .baseUrl(MEAL_DB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(MealDbApi::class.java)
    }
    fun fetchBooksAndMeals(
        application: Application,
        bookSearchTerm: String,
        mealSearchTerm: String,
        page: Int = 0
    ): Single<Pair<BooksResponse, MealResponse>> {
        val googleBooksApi = provideGoogleBooksApi(application)
        val mealDbApi = provideMealDbApi(application)

        return Single.zip(
            googleBooksApi.searchBooks(bookSearchTerm, page * 10), // Fetch 10 books per page
            mealDbApi.searchMeals(mealSearchTerm, page + 1)
        ) // Fetch meals with page index
        { booksResponse: BooksResponse, mealResponse: MealResponse ->
            Pair(booksResponse, mealResponse)
        }
    }
}