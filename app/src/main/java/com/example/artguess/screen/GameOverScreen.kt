package com.example.artguess.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameOverScreen(
    score: Int,
    total: Int,
    streak: Int,
    onPlayAgain: () -> Unit
) {
    val rank = when {
        score >= total -> "Master Curator"
        score >= (total * 0.8).toInt() -> "Art Historian"
        score >= (total * 0.6).toInt() -> "Gallery Regular"
        score >= (total * 0.4).toInt() -> "Museum Wanderer"
        else -> "Casual Observer"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Over", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Final Score: $score / $total",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Best Streak: $streak",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary.copy(alpha = 0.8f)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Rank: $rank",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary.copy(alpha = 0.8f)
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onPlayAgain,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonIdle, contentColor = TextPrimary),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text("Play Again", color = TextPrimary)
        }
    }
}
