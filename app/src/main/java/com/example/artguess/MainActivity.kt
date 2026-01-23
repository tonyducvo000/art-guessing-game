package com.example.artguess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.artguess.game.GameViewModel
import com.example.artguess.ui.ArtGuessingGameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val vm: GameViewModel = viewModel()
                    ArtGuessingGameScreen(viewModel = vm)
                }
            }
        }
    }
}
