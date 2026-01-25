package com.example.artguess.game

import com.example.artguess.data.Round

data class GameUIState(
    val round: Round? = null,
    val selected: String? = null,
    val score: Int = 0,
    val streak: Int = 0,
    val seenIds: Set<String> = emptySet(),
    val totalRounds: Int = 10,
    val roundNumber: Int = 0,
    val isGameOver: Boolean = false,
    val isLoading: Boolean = false,
    val isGameStarted: Boolean = false
) {
    val correctArtist: String? get() = round?.artwork?.artist
    val isAnswered: Boolean get() = selected != null
}
