package com.example.artguess.game

import com.example.artguess.data.Round

data class GameUiState(
    val round: Round,
    val selected: String? = null,
    val score: Int = 0,
    val streak: Int = 0,
    val lastArtworkId: String? = null,
    val seenIds: Set<String> = emptySet(),   // ⭐ no-repeat tracking
    val roundNumber: Int = 1                 // ⭐ progress
) {
    val correctArtist: String get() = round.artwork.artist
    val isAnswered: Boolean get() = selected != null
}
