package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VideoWatchDialog(
    videosLeft: Int,
    onDismiss: () -> Unit,
    onVideoComplete: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableStateOf(10) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        if (isPlaying && !isFinished) {
            while (secondsRemaining > 0) {
                delay(1000)
                secondsRemaining -= 1
            }
            isFinished = true
            isPlaying = false
            onVideoComplete()
        }
    }

    Dialog(onDismissRequest = { if (!isPlaying) onDismiss() }) {
        GlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("video_watch_dialog"),
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
                        Text(text = "🎬", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ভিডিও ওয়াচ টাস্ক",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 17.sp
                            )
                        )
                    }
                    IconButton(
                        onClick = { if (!isPlaying) onDismiss() },
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
                    text = "১০ সেকেন্ডের ভিডিওটি সম্পূর্ণ দেখুন এবং ৩০ পয়েন্ট সংগ্রহ করুন",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated Video Player Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E1B4B)))
                        )
                        .border(1.dp, if (isPlaying) NeonCyan else GlassBorderLight, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isPlaying && !isFinished) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(NeonCyan, ElectricViolet))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "বিজ্ঞাপন ভিডিও রেডি (+৩০ পয়েন্ট)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    } else if (isPlaying) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                progress = { (10 - secondsRemaining) / 10f },
                                modifier = Modifier.size(60.dp),
                                color = NeonCyan,
                                trackColor = Color(0x30FFFFFF),
                                strokeWidth = 5.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "ভিডিও চলছে... $secondsRemaining সেকেন্ড বাকি",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = NeonCyanGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = "টাকাফ্লো ডিজিটাল স্পন্সরড প্রিভিউ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    } else {
                        // Finished
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = NeonEmeraldLight,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "টাস্ক সম্পন্ন! +৩০ পয়েন্ট যোগ হয়েছে",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = NeonEmeraldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GlassPrimaryButton(
                    text = if (isFinished) "বন্ধ করুন" else if (isPlaying) "অনুগ্রহ করে অপেক্ষা করুন..." else "ভিডিও দেখুন ও পয়েন্ট নিন",
                    onClick = {
                        if (isFinished) {
                            onDismiss()
                        } else if (!isPlaying) {
                            isPlaying = true
                        }
                    },
                    enabled = !isPlaying,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "video_play_button"
                )
            }
        }
    }
}
