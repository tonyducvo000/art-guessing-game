package com.example.artguess.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.artguess.game.GameViewModel

private val ButtonIdle = Color(0xFFD1D1D1)
private val ButtonCorrect = Color(0xFFDCEEDC)
private val ButtonWrong = Color(0xFFF2DADA)
private val TextPrimary = Color.Black

@Composable
fun ArtGuessingGameScreen(viewModel: GameViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    val round = state.round
    val selected = state.selected
    val correct = state.correctArtist

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // TOP: title + image area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Who made this?",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = round.artwork.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = round.artwork.title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // BOTTOM: choices anchored higher (padding bottom lifts them)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                round.choices.forEach { choice ->
                    val isCorrect = choice == correct
                    val isSelected = selected == choice

                    val container = when {
                        selected == null -> ButtonIdle
                        isCorrect -> ButtonCorrect
                        isSelected -> ButtonWrong
                        else -> ButtonIdle
                    }

                    Button(
                        onClick = { viewModel.select(choice) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = container,
                            contentColor = TextPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(choice, color = TextPrimary)
                    }
                }
            }

            if (selected != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (selected == correct) "Correct"
                    else "Incorrect — it’s $correct",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.nextRound() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonIdle,
                        contentColor = TextPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Next", color = TextPrimary)
                }
            }
        }
    }
}
