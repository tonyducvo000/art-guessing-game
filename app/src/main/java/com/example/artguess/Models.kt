package com.example.artguess

data class Artwork(
    val id: String,
    val title: String,
    val imageUrl: String,
    val artist: String
)

data class Round(
    val artwork: Artwork,
    val choices: List<String>
)
