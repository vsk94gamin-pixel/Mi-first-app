package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.TakaFlowRepository
import com.example.model.QuizQuestion
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun QuizDialog(
    quizzesLeft: Int,
    onDismiss: () -> Unit,
    onQuizSuccess: () -> Unit
) {
    val questions = remember { TakaFlowRepository.quizQuestions }
    var currentQuestionIndex by remember { mutableStateOf((0 until questions.size).random()) }
    val currentQuestion = questions[currentQuestionIndex]

    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableStateOf(20) }

    LaunchedEffect(currentQuestionIndex, isSubmitted) {
        if (!isSubmitted) {
            secondsLeft = 20
            while (secondsLeft > 0 && !isSubmitted) {
                delay(1000)
                secondsLeft -= 1
            }
            if (secondsLeft == 0 && !isSubmitted) {
                isSubmitted = true
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        GlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("quiz_dialog"),
            cornerRadius = 28.dp,
            backgroundColor = Color(0xF00A0F1E),
            borderColor = GlassBorderHighlight,
            contentPadding = PaddingValues(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🧠", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ম্যাথ ও জিকে কুইজ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 17.sp
                            )
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                // Timer & Reward Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x30F59E0B))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = AmberGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$secondsLeft সেকেন্ড",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = AmberGoldLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "পুরস্কার: +২০ পয়েন্ট",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = NeonCyanGlow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Question Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x2538BDF8))
                        .border(1.dp, GlassBorderHighlight, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentQuestion.questionBn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    currentQuestion.options.forEachIndexed { index, optionText ->
                        val isSelected = selectedOptionIndex == index
                        val isCorrect = index == currentQuestion.correctIndex

                        val optionBg = when {
                            isSubmitted && isCorrect -> Color(0x4010B981)
                            isSubmitted && isSelected && !isCorrect -> Color(0x40F43F5E)
                            isSelected -> Color(0x3506B6D4)
                            else -> Color(0x18FFFFFF)
                        }

                        val optionBorder = when {
                            isSubmitted && isCorrect -> NeonEmerald
                            isSubmitted && isSelected && !isCorrect -> CoralRose
                            isSelected -> NeonCyan
                            else -> GlassBorderLight
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(optionBg)
                                .border(1.dp, optionBorder, RoundedCornerShape(14.dp))
                                .clickable(enabled = !isSubmitted) {
                                    selectedOptionIndex = index
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) NeonCyan else Color(0x20FFFFFF)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${('ক' + index)}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) Color.Black else TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Result / Explanation
                if (isSubmitted) {
                    val wasCorrect = selectedOptionIndex == currentQuestion.correctIndex
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (wasCorrect) Color(0x2810B981) else Color(0x28F43F5E))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (wasCorrect) "🎉 দারুণ! সঠিক উত্তর হয়েছে (+২০ পয়েন্ট যোগ হয়েছে)" else "❌ ভুল উত্তর! সঠিক উত্তর ছিল: ${currentQuestion.options[currentQuestion.correctIndex]}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (wasCorrect) NeonEmeraldLight else CoralRose,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQuestion.explanationBn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Submit / Next Button
                GlassPrimaryButton(
                    text = if (!isSubmitted) "উত্তর নিশ্চিত করুন" else "পরবর্তী প্রশ্ন খেলুন",
                    onClick = {
                        if (!isSubmitted) {
                            if (selectedOptionIndex != null) {
                                isSubmitted = true
                                if (selectedOptionIndex == currentQuestion.correctIndex) {
                                    onQuizSuccess()
                                }
                            }
                        } else {
                            // Reset for next question
                            currentQuestionIndex = (questions.indices - currentQuestionIndex).randomOrNull() ?: 0
                            selectedOptionIndex = null
                            isSubmitted = false
                        }
                    },
                    enabled = isSubmitted || selectedOptionIndex != null,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "quiz_submit_button"
                )
            }
        }
    }
}
