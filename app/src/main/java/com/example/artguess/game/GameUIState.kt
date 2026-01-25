package com.example.artguess.game

import com.example.artguess.data.Round

data class GameUIState(
    val round: Round,
    val selected: String? = null,
    val score: Int = 0,
    val streak: Int = 0,
    val lastArtworkId: String? = null,
    val seenIds: Set<String> = emptySet(),   // ⭐ no-repeat tracking
    val viewedIds: Set<Int> = emptySet(), // Use emptySet here!
    val totalRounds: Int,
    val roundNumber: Int = 1,                 // ⭐ progress
    val isGameOver: Boolean = false
) {
    val correctArtist: String get() = round.artwork.artist
    val isAnswered: Boolean get() = selected != null
}
