package com.example.pagesplates.views

import android.util.Log
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.MaterialTheme
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pagesplates.Data.Book
import com.example.pagesplates.Data.Meal
import com.example.pagesplates.Routes
import com.example.pagesplates.viewmodels.MainViewModel

@Composable
fun BooksAndRecipesScreen(viewModel: MainViewModel, navController: NavHostController) {
    // State for toggle
    var isBooksTab by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }

    // Observe LiveData from ViewModel
    val books = viewModel.books.observeAsState(emptyList())
    val meals = viewModel.meals.observeAsState(emptyList())
    val isLoading = viewModel.loading.observeAsState(false)

    // Initialize LazyListState for scroll tracking
    val listState = rememberLazyListState()

    // Trigger data fetch on first load
    LaunchedEffect(Unit) {
        viewModel.fetchBooksAndMeals("fiction", "")
    }

    // Trigger load more data when scrolled to the end
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        if (lastVisibleItemIndex != null) {
            if (lastVisibleItemIndex == books.value.size - 1 && selectedTab == 0) {
                viewModel.loadMoreData("fiction", "") // Pass the params as needed
            } else if (lastVisibleItemIndex == meals.value.size - 1 && selectedTab == 1) {
                viewModel.loadMoreData("fiction", "") // Pass the params as needed
            }
        }
    }
    Column {
        // Toggle Button or Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                selectedTab = 0
                isBooksTab = true
            },
                modifier = Modifier.weight(1f).padding(5.dp)) {
                Text("Books")
            }
            Button(onClick = {
                selectedTab = 1
                isBooksTab = false
            }, modifier = Modifier.weight(1f)) {
                Text("Recipes")
            }
        }

        // Display Shimmer while Loading
        if (isLoading.value && (books.value.isEmpty() && meals.value.isEmpty())) {
            ShimmerLoadingList()
        } else {
            if (isBooksTab) {
                if (books.value.isEmpty()) {
                    Text("No books found.")
                } else {
                    LazyColumn(state = listState) {
                        items(books.value) { book -> BookItem(book){
                            navController.navigate(Routes.BookDetailsScreen.createRoute(book.id))
                        }
                        }
                    }
                }
            } else {
                if (meals.value.isEmpty()) {
                    Text("No meals found.")
                } else {
                    LazyColumn(state = listState) {
                        items(meals.value) { meal -> RecipeItem(meal){
                            navController.navigate(Routes.MealDetailsScreen.createRoute(meal.idMeal))
                        }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)

    )
    {
        Row(modifier = Modifier.padding(8.dp)) {
            // Display book thumbnail
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                update = { imageView ->
                    val imageUrl = book.volumeInfo.imageLinks?.thumbnail
                    Log.d("GlideDebug", "Image URL: $imageUrl")
                    Glide.with(imageView.context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView)

                },

                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )




            Spacer(modifier = Modifier.width(8.dp))
            // Display book details
            Column {
                Text(text = book.volumeInfo.title, style = MaterialTheme.typography.titleMedium,)
                val authors = book.volumeInfo.authors.joinToString(", ") ?: "Unknown Authors"
                Text(text = authors)
            }
        }
    }

}

@Composable
fun RecipeItem(meal: Meal,onClick: () -> Unit) {
    Card(modifier = Modifier.
    clickable { onClick() }
        .fillMaxWidth()
        .padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Display recipe thumbnail
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = "Meal Thumbnail",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Display recipe details
            Column {
                Text(text = meal.strMeal, style = MaterialTheme.typography.titleMedium)
                Text(text = meal.strCategory)
            }
        }
    }

}
@Composable
fun ShimmerLoadingList() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(10) { // You can adjust the number of items
            ShimmerListItem(brush)
        }
    }
}

@Composable
fun ShimmerListItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(80.dp).height(80.dp)
                .background(brush, shape = RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(brush, shape = RoundedCornerShape(4.dp))
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .background(brush, shape = RoundedCornerShape(4.dp))
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .background(brush, shape = RoundedCornerShape(4.dp))
            )
        }
    }
}


