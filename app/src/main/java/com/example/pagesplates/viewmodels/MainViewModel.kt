package com.example.pagesplates.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pagesplates.Data.Book
import com.example.pagesplates.Data.Meal
import com.example.pagesplates.Object.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val disposables = CompositeDisposable()

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    private val _error = MutableLiveData<String>() // LiveData for error messages
    val error: LiveData<String> get() = _error
    private val apiService = ApiService
    private var currentPage = 0

    fun fetchBooksAndMeals(  bookSearchTerm: String, mealSearchTerm: String) {
        _loading.value = true
        apiService.fetchBooksAndMeals(getApplication(), bookSearchTerm, mealSearchTerm, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _loading.value = false
                Log.d("Books Response", "Books count: ${response.first.items.size}")
                Log.d("Meals Response", "Meals count: ${response.second.meals?.size}")

                Log.d("Books", "Fetched books: ${response.first.items}")
                _books.value = response.first.items
                _meals.value = response.second.meals ?: emptyList()
            }, { error ->
                _loading.value = false
                Log.d("Error", error.message ?: "Unknown error")
                // Enhanced error handling
                if (error is IOException) {
                    _error.value = "No internet connection. Please turn on your data or connect to Wi-Fi."
                } else {
                    _error.value = "Something went wrong. Please try again later."
                }
            }).also { disposables.add(it) }
    }

    fun loadMoreData(bookSearchTerm: String, mealSearchTerm: String) {
        currentPage++
        fetchBooksAndMeals( bookSearchTerm, mealSearchTerm)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}