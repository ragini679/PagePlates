package com.example.pagesplates.Data

data class MealResponse(
    val meals: List<Meal>?
)

data class Meal(
    val idMeal: String = "",
    val strMeal: String = "Unknown Meal",
    val strCategory: String = "Unknown Category",
    val strMealThumb: String = "",
    val strInstructions: String = "No Instructions Available",
    val strIngredients1:String=""
)


