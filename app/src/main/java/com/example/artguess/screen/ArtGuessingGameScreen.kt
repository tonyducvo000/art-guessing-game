package com.example.artguess.screen



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
                .weight(1f) // Allow the top section to occupy available space
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Who made this?",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Score: ${state.score}/${state.totalRounds}  ·  Streak: ${state.streak}",
                // Changed from labelMedium to titleMedium for a larger font size
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary.copy(alpha = 0.6f),
                // Added SemiBold weight to make it stand out without being as heavy as the title
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = round.artwork.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Image grows/shrinks to fit the column
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = round.artwork.title,
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTTOM: choices + feedback (Takes exact space needed)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = container,
                        contentColor = TextPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = choice,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (selected != null) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (selected == correct) "Correct"
                    else "Incorrect — it’s $correct",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = { viewModel.nextRound() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonIdle,
                        contentColor = TextPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Next",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
