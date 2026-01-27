package com.example.artguess.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artguess.data.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState

    init {
        loadStartArtwork()
    }

    private fun loadStartArtwork() {
        viewModelScope.launch {
            try {
                val artwork = ArtRepository.getRandomArtwork()
                _uiState.update { it.copy(startArtwork = artwork) }
            } catch (e: Exception) {
                // Ignore failure for start screen decoration
            }
        }
    }

    fun startGame() {
        _uiState.update { it.copy(isGameStarted = true, isLoading = true) }
        loadNextRound()
    }

    private fun loadNextRound() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val nextRound = ArtRepository.nextRound(excludeIds = _uiState.value.seenIds)
                _uiState.update { state ->
                    state.copy(
                        round = nextRound,
                        selected = null,
                        seenIds = state.seenIds + nextRound.artwork.id,
                        roundNumber = state.roundNumber + 1,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun select(choice: String) {
        _uiState.update { state ->
            val correct = state.correctArtist ?: return@update state
            if (state.isAnswered) return@update state

            val isCorrect = (choice == correct)
            val newStreak = if (isCorrect) state.streak + 1 else 0

            state.copy(
                selected = choice,
                score = state.score + if (isCorrect) 1 else 0,
                streak = newStreak,
                maxStreak = max(state.maxStreak, newStreak)
            )
        }
    }

    fun nextRound() {
        if (_uiState.value.roundNumber >= _uiState.value.totalRounds) {
            _uiState.update { it.copy(isGameOver = true) }
        } else {
            loadNextRound()
        }
    }

    fun restartGame() {
        _uiState.value = GameUIState(isGameStarted = true, isLoading = true)
        loadNextRound()
    }
}
