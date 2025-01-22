package com.example.pagesplates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pagesplates.detailsscreen.BookDetailsScreen
import com.example.pagesplates.detailsscreen.MealDetailsScreen
import com.example.pagesplates.ui.theme.PagesPlatesTheme
import com.example.pagesplates.viewmodels.MainViewModel
import com.example.pagesplates.views.BooksAndRecipesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagesPlatesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                }
            }
        }
    }
}
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination =Routes.BooksAndRecipesScreen.route ){
        composable(Routes.BooksAndRecipesScreen.route){
            BooksAndRecipesScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            Routes.BookDetailsScreen.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            BookDetailsScreen(viewModel = viewModel, bookId = bookId)
        }
        composable(
            Routes.MealDetailsScreen.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable
            MealDetailsScreen(viewModel = viewModel, mealId = mealId)
        }
    }
}
