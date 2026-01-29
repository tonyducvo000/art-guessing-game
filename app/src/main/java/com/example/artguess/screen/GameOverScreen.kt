package com.example.artguess.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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

    // Configure the confetti party
    val parties = remember {
        listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb980ff),
                position = Position.Relative(0.5, 0.3),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Confetti layer
        if (score >= (total * 0.6).toInt()) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = parties,
            )
        }

        // Content layer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Game Over", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Final Score: $score / $total",
                style = MaterialTheme.typography.headlineSmall,
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

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = onPlayAgain,
                modifier = Modifier.height(56.dp).padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonIdle, contentColor = TextPrimary),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Play Again", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            }
        }
    }
}
