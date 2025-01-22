package com.example.pagesplates.detailsscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import coil.compose.rememberAsyncImagePainter
import com.example.pagesplates.viewmodels.MainViewModel


@Composable
fun BookDetailsScreen(viewModel: MainViewModel, bookId: String) {
    // Find the book with the matching ID
    val book = viewModel.books.value?.find { it.id == bookId }
    if (book != null) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                // Access properties from the `volumeInfo` object

                Text(text = "Title: ${book.volumeInfo.title}",style = MaterialTheme.typography.titleLarge )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Authors: ${book.volumeInfo.authors.joinToString(", ")}",style = MaterialTheme.typography.titleSmall,)
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Publisher: ${book.volumeInfo.publisher}",style = MaterialTheme.typography.titleSmall,)
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Description: ${book.volumeInfo.description}")

                // Display the image if available
                book.volumeInfo.imageLinks?.let { imageLinks ->
                    Image(
                        painter = rememberAsyncImagePainter(model = imageLinks.thumbnail),
                        contentDescription = null,
                    )
                }
            }
        }

    } else {
        Text("Book not found.")
    }
}
