package com.example.pagesplates.detailsscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import coil.compose.AsyncImage
import com.example.pagesplates.viewmodels.MainViewModel


@Composable
fun MealDetailsScreen(viewModel: MainViewModel, mealId: String) {
    // Observe the meals LiveData from the ViewModel
    val meals = viewModel.meals.observeAsState(emptyList())

    // Find the meal by ID
    val selectedMeal = meals.value.find { it.idMeal == mealId }
    if (selectedMeal != null) {
        // Display the details of the selected meal
        Log.e("MealDetails", "Selected meal: $selectedMeal")
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            // Display the image of the meal
            AsyncImage(
                model = selectedMeal.strMealThumb,
                contentDescription = selectedMeal.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Meal Name
            Text(
                text = selectedMeal.strMeal,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category
            Text(
                text = "Category: ${selectedMeal.strCategory}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredients
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedMeal.strIngredients1,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedMeal.strInstructions,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        // Show a message if the meal details are not found
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Meal details not found.",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Red
            )
        }
    }
}
