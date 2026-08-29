package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SpinWheelDialog(
    spinsLeft: Int,
    onDismiss: () -> Unit,
    onSpinComplete: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }
    var wonPrize by remember { mutableStateOf<Int?>(null) }

    // Slices on wheel: (Points, Color)
    val slices = listOf(
        Pair(20, Color(0xFF06B6D4)),
        Pair(50, Color(0xFF8B5CF6)),
        Pair(10, Color(0xFF10B981)),
        Pair(200, Color(0xFFF59E0B)),
        Pair(30, Color(0xFFEC4899)),
        Pair(100, Color(0xFF3B82F6)),
        Pair(15, Color(0xFF14B8A6)),
        Pair(500, Color(0xFFEAB308))
    )
    val sliceAngle = 360f / slices.size

    Dialog(onDismissRequest = { if (!isSpinning) onDismiss() }) {
        GlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("spin_wheel_dialog"),
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
                        Text(text = "🎡", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "লাকি স্পিন হুইল",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 18.sp
                            )
                        )
                    }
                    IconButton(
                        onClick = { if (!isSpinning) onDismiss() },
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
                    text = "আজকের বাকি স্পিন: $spinsLeft বার",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = NeonCyanGlow,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Animated Wheel Canvas
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Top Pointer indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-6).dp)
                            .size(24.dp, 28.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val path = Path().apply {
                                moveTo(size.width / 2, size.height)
                                lineTo(0f, 0f)
                                lineTo(size.width, 0f)
                                close()
                            }
                            drawPath(path, color = AmberGoldLight)
                        }
                    }

                    // Rotating Canvas Wheel
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotation.value)
                    ) {
                        val canvasRadius = size.minDimension / 2f
                        val centerOffset = Offset(size.width / 2f, size.height / 2f)

                        // Draw outer glow border
                        drawCircle(
                            color = Color(0x5038BDF8),
                            radius = canvasRadius + 4f,
                            style = Stroke(width = 6f)
                        )

                        // Draw slice sectors
                        slices.forEachIndexed { i, slice ->
                            val startAngle = i * sliceAngle
                            drawArc(
                                color = slice.second,
                                startAngle = startAngle,
                                sweepAngle = sliceAngle,
                                useCenter = true,
                                topLeft = Offset(centerOffset.x - canvasRadius, centerOffset.y - canvasRadius),
                                size = Size(canvasRadius * 2, canvasRadius * 2),
                                style = Fill
                            )
                            drawArc(
                                color = Color(0x30FFFFFF),
                                startAngle = startAngle,
                                sweepAngle = sliceAngle,
                                useCenter = true,
                                topLeft = Offset(centerOffset.x - canvasRadius, centerOffset.y - canvasRadius),
                                size = Size(canvasRadius * 2, canvasRadius * 2),
                                style = Stroke(width = 1.5f)
                            )
                        }

                        // Draw points numbers on each slice
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 34f
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                            textAlign = android.graphics.Paint.Align.CENTER
                        }

                        slices.forEachIndexed { i, slice ->
                            val midAngleRad = Math.toRadians((i * sliceAngle + sliceAngle / 2f).toDouble())
                            val textDist = canvasRadius * 0.65f
                            val textX = centerOffset.x + (textDist * cos(midAngleRad)).toFloat()
                            val textY = centerOffset.y + (textDist * sin(midAngleRad)).toFloat() + 10f

                            drawContext.canvas.nativeCanvas.drawText(
                                "${slice.first}",
                                textX,
                                textY,
                                paint
                            )
                        }

                        // Center glowing knob
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(DarkBgStart, Color(0xFF1E293B)),
                                center = centerOffset,
                                radius = 28f
                            ),
                            radius = 28f,
                            center = centerOffset
                        )
                        drawCircle(
                            color = NeonCyan,
                            radius = 28f,
                            center = centerOffset,
                            style = Stroke(width = 3f)
                        )
                    }

                    // Center Token
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkBgStart)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🪙", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Result Box or Instructions
                if (wonPrize != null) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3010B981))
                            .border(1.dp, NeonEmerald, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎉", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "অভিনন্দন! আপনি $wonPrize পয়েন্ট জিতেছেন!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonEmeraldLight,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Spin Action Button
                GlassPrimaryButton(
                    text = if (isSpinning) "হুইল ঘুরছে..." else if (wonPrize != null) "সংগ্রহ করুন & আবার ঘুরান" else "স্পিন করুন (Spin Now)",
                    onClick = {
                        if (wonPrize != null) {
                            wonPrize = null
                        }
                        if (spinsLeft <= 0) return@GlassPrimaryButton

                        isSpinning = true
                        coroutineScope.launch {
                            val chosenSliceIndex = (0 until slices.size).random()
                            val targetSlice = slices[chosenSliceIndex]

                            // Calculate target angle to point pointer (top = 270 deg)
                            val sliceCenterAngle = chosenSliceIndex * sliceAngle + (sliceAngle / 2f)
                            val targetRotation = (360f * 5) + (270f - sliceCenterAngle)

                            rotation.animateTo(
                                targetValue = rotation.value + targetRotation + Random.nextFloat() * 10f,
                                animationSpec = tween(
                                    durationMillis = 3500,
                                    easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)
                                )
                            )

                            isSpinning = false
                            wonPrize = targetSlice.first
                            onSpinComplete(targetSlice.first)
                        }
                    },
                    enabled = !isSpinning && spinsLeft > 0,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Refresh,
                    testTag = "spin_action_button"
                )
            }
        }
    }
}
