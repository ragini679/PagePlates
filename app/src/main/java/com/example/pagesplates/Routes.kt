package com.example.pagesplates

sealed class Routes(val route: String) {
    object BooksAndRecipesScreen : Routes("books_and_recipes_screen")
    object BookDetailsScreen : Routes("book_details_screen/{bookId}") {
        fun createRoute(bookId: String) = "book_details_screen/$bookId"
    }
    object MealDetailsScreen : Routes("meal_details_screen/{mealId}") {
        fun createRoute(mealId: String) = "meal_details_screen/$mealId"
    }
}