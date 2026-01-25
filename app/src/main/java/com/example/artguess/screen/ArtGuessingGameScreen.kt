package com.example.artguess.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.artguess.game.GameViewModel

@Composable
fun ArtGuessingGameScreen(viewModel: GameViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = TextPrimary)
        }
    } else if (state.isGameOver) {
        GameOverScreen(
            score = state.score,
            total = state.totalRounds,
            onPlayAgain = { viewModel.restartGame() }
        )
    } else {
        val round = state.round
        if (round != null) {
            val selected = state.selected
            val correct = state.correctArtist

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp) // Slightly reduced padding
            ) {
                // TOP: title + image area + feedback
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Allow this section to take up available space
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Who made this?",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineMedium, // Slightly smaller than displaySmall
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Score: ${state.score}/${state.totalRounds}  ·  Streak: ${state.streak}",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary.copy(alpha = 0.6f),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AsyncImage(
                        model = round.artwork.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Image shrinks/grows to fit everything else
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = round.artwork.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )

                    // Answer and Next Button below image/title
                    if (selected != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selected == correct) "Correct!"
                            else "Incorrect — it's $correct",
                            fontWeight = FontWeight.Bold,
                            color = if (selected == correct) Color(0xFF2E7D32) else Color(0xFFC62828),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.nextRound() },
                            modifier = Modifier
                                .fillMaxWidth(0.6f) // Slightly narrower for better look
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonIdle,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(24.dp), // Pill shaped
                            elevation = ButtonDefaults.buttonElevation(2.dp)
                        ) {
                            Text(
                                text = if (state.roundNumber >= state.totalRounds) "Finish" else "Next",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTTOM: choices
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    round.choices.forEach { choice ->
                        val isCorrect = choice == correct
                        val isSelected = selected == choice

                        // Shake animation logic
                        val shakeOffset = remember { Animatable(0f) }
                        LaunchedEffect(isSelected) {
                            if (isSelected && !isCorrect) {
                                shakeOffset.animateTo(
                                    targetValue = 0f,
                                    animationSpec = keyframes {
                                        durationMillis = 400
                                        -10f at 50
                                        10f at 100
                                        -10f at 150
                                        10f at 200
                                        -10f at 250
                                        10f at 300
                                        -5f at 350
                                    }
                                )
                            }
                        }

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
                                .height(56.dp) // Slightly shorter buttons to save space
                                .offset(x = shakeOffset.value.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = container,
                                contentColor = TextPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(1.dp)
                        ) {
                            AutoResizeText(
                                text = choice,
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AutoResizeText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = 1
) {
    var resizedTextStyle by remember(text) { mutableStateOf(style) }
    var shouldDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) drawContent()
        },
        fontWeight = fontWeight,
        textAlign = textAlign,
        softWrap = false,
        maxLines = maxLines,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (resizedTextStyle.fontSize.isSp && resizedTextStyle.fontSize.value > 10) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = (resizedTextStyle.fontSize.value * 0.9f).sp
                    )
                } else {
                    shouldDraw = true
                }
            } else {
                shouldDraw = true
            }
        }
    )
}
