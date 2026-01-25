package com.example.artguess.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artguess.data.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUIState(isLoading = true))
    val uiState: StateFlow<GameUIState> = _uiState

    init {
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
                // Handle error (e.g., set an error state)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun select(choice: String) {
        _uiState.update { state ->
            val correct = state.correctArtist ?: return@update state
            if (state.isAnswered) return@update state

            val isCorrect = (choice == correct)

            state.copy(
                selected = choice,
                score = state.score + if (isCorrect) 1 else 0,
                streak = if (isCorrect) state.streak + 1 else 0
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
        _uiState.value = GameUIState(isLoading = true)
        loadNextRound()
    }
}
