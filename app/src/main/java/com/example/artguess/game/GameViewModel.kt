package com.example.artguess.game

import androidx.lifecycle.ViewModel
import com.example.artguess.data.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUIState(
            round = ArtRepository.nextRound(excludeId = null),
            selected = null,
            score = 0,
            streak = 0,
            viewedIds = emptySet(),
            lastArtworkId = null,
            totalRounds = ArtRepository.artWorkSize()
        )
    )

    val uiState: StateFlow<GameUIState> = _uiState

    fun select(choice: String) {
        _uiState.update { state ->
            if (state.isAnswered) return@update state

            val correct = state.correctArtist
            val isCorrect = (choice == correct)

            state.copy(
                selected = choice,
                score = state.score + if (isCorrect) 1 else 0,
                streak = if (isCorrect) state.streak + 1 else 0
            )
        }
    }

    fun nextRound() {
        _uiState.update { state ->
            val newRound = ArtRepository.nextRound(excludeId = state.round.artwork.id)
            state.copy(
                round = newRound,
                selected = null,
                lastArtworkId = newRound.artwork.id,
                roundNumber = state.roundNumber + 1
            )
        }
    }
}
