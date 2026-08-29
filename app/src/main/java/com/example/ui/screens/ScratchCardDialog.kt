package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.theme.*

@Composable
fun ScratchCardDialog(
    scratchCardsLeft: Int,
    onDismiss: () -> Unit,
    onScratchComplete: (Int) -> Unit
) {
    var scratchProgress by remember { mutableStateOf(0f) }
    var isRevealed by remember { mutableStateOf(false) }
    val hiddenPoints = remember { listOf(25, 50, 75, 100, 150, 200, 300).random() }

    Dialog(onDismissRequest = onDismiss) {
        GlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("scratch_card_dialog"),
            cornerRadius = 28.dp,
            backgroundColor = Color(0xF00A0F1E),
            borderColor = AmberGold,
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
                        Text(text = "✨", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "গোল্ডেন স্ক্র্যাচ কার্ড",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberGoldLight,
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

                Text(
                    text = "আঙুল দিয়ে ঘষে ভেতরের পয়েন্ট রিভিল করুন! বাকি: $scratchCardsLeft টি",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Scratch Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.5.dp, AmberGoldLight, RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1E1B4B), Color(0xFF0F172A)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Hidden Reward Content underneath
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🏆", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "+$hiddenPoints পয়েন্ট",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberGoldLight,
                                fontSize = 26.sp
                            )
                        )
                        Text(
                            text = "অভিনন্দন! আপনার ওয়ালেটে যোগ হয়েছে",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonEmeraldLight,
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Scratch Cover Layer
                    if (!isRevealed) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFFEAB308),
                                            Color(0xFFF59E0B),
                                            Color(0xFFB45309)
                                        )
                                    ),
                                    alpha = (1f - (scratchProgress / 100f)).coerceAtLeast(0.1f)
                                )
                                .pointerInput(Unit) {
                                    detectDragGestures { _, dragAmount ->
                                        val distance = (dragAmount.x * dragAmount.x + dragAmount.y * dragAmount.y)
                                        if (distance > 2f) {
                                            scratchProgress = (scratchProgress + 6f).coerceAtMost(100f)
                                            if (scratchProgress >= 65f && !isRevealed) {
                                                isRevealed = true
                                                onScratchComplete(hiddenPoints)
                                            }
                                        }
                                    }
                                }
                                .clickable {
                                    // Tap to reveal helper
                                    scratchProgress = 100f
                                    isRevealed = true
                                    onScratchComplete(hiddenPoints)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "এখানে ঘষুন বা ট্যাপ করুন",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "স্ক্র্যাচ কমপ্লিট: ${scratchProgress.toInt()}%",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFFEF08A),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GlassPrimaryButton(
                    text = if (isRevealed) "রিওয়ার্ড ক্লেইম হয়েছে" else "সরাসরি রিভিল করুন",
                    onClick = {
                        if (!isRevealed) {
                            isRevealed = true
                            scratchProgress = 100f
                            onScratchComplete(hiddenPoints)
                        } else {
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(AmberGold, Color(0xFFE11D48)),
                    testTag = "scratch_action_button"
                )
            }
        }
    }
}
