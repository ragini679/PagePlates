package com.example.pagesplates.Data

data class BooksResponse(
    val items: List<Book>
)
data class Book(
    val id: String,
    var volumeInfo:VolumeInfo
)
data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val description: String,
    val imageLinks: ImageLinks? // Optional field
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String,
    val small: String,
    val medium: String,
    val large: String,
    val extraLarge: String
)


