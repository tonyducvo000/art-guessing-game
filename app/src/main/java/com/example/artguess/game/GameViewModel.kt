package com.example.artguess.game

import androidx.lifecycle.ViewModel
import com.example.artguess.data.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtRepository.nextRound().let { firstRound ->
            GameUIState(
                round = firstRound,
                seenIds = setOf(firstRound.artwork.id),
                totalRounds = ArtRepository.artWorkSize()
            )
        }
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
            if (state.roundNumber >= state.totalRounds) {
                return@update state.copy(isGameOver = true)
            }

            val newRound = ArtRepository.nextRound(excludeIds = state.seenIds)
            state.copy(
                round = newRound,
                selected = null,
                seenIds = state.seenIds + newRound.artwork.id,
                roundNumber = state.roundNumber + 1
            )
        }
    }

    fun restartGame() {
        val firstRound = ArtRepository.nextRound()
        _uiState.value = GameUIState(
            round = firstRound,
            seenIds = setOf(firstRound.artwork.id),
            totalRounds = ArtRepository.artWorkSize()
        )
    }
}
