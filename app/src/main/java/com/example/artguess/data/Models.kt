package com.example.artguess.data

data class Artwork(
    val id: String,
    val title: String,
    val imageUrl: String,
    val artist: String,
    val endDate: Int? = null
)

data class Round(
    val artwork: Artwork,
    val choices: List<String>
)
