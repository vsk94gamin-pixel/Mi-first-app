package com.example.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ==========================================
// TAKAFLOW ULTRA PREMIUM GLASS COLOR PALETTE
// ==========================================

val DarkBgStart = Color(0xFF0A0F1E)
val DarkBgMiddle = Color(0xFF0F172A)
val DarkBgEnd = Color(0xFF131127)

val GlassSurface = Color(0x1CFFFFFF)
val GlassSurfaceElevated = Color(0x2EFFFFFF)
val GlassSurfaceDark = Color(0x35080E1C)
val GlassBorderLight = Color(0x38FFFFFF)
val GlassBorderHighlight = Color(0x6038BDF8)
val GlassBorderGold = Color(0x60F59E0B)

val NeonCyan = Color(0xFF06B6D4)
val NeonCyanGlow = Color(0xFF38BDF8)
val NeonEmerald = Color(0xFF10B981)
val NeonEmeraldLight = Color(0xFF34D399)
val ElectricViolet = Color(0xFF8B5CF6)
val ElectricPurple = Color(0xFFA855F7)
val AmberGold = Color(0xFFF59E0B)
val AmberGoldLight = Color(0xFFFBBF24)
val CoralRose = Color(0xFFF43F5E)

// Brand Specific Colors
val BkashPink = Color(0xFFE2136E)
val NagadOrange = Color(0xFFF7941D)
val RocketPurple = Color(0xFF8C3494)
val TelegramBlue = Color(0xFF229ED9)

// Status Colors
val StatusPendingBg = Color(0x28F59E0B)
val StatusPendingText = Color(0xFFFBBF24)
val StatusProcessingBg = Color(0x2806B6D4)
val StatusProcessingText = Color(0xFF38BDF8)
val StatusApprovedBg = Color(0x2810B981)
val StatusApprovedText = Color(0xFF34D399)
val StatusRejectedBg = Color(0x28F43F5E)
val StatusRejectedText = Color(0xFFFB7185)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val TakaFlowColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0E7490),
    onPrimaryContainer = Color(0xFFCFFAFE),
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF581C87),
    tertiary = AmberGold,
    background = DarkBgStart,
    surface = DarkBgMiddle,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextSecondary
)

@Composable
fun TakaFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TakaFlowColorScheme,
        typography = Typography,
        content = content
    )
}

// ==========================================
// GLASSMORPHISM MODIFIER EXTENSIONS
// ==========================================

fun Modifier.glassCard(
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = GlassSurface,
    borderColor: Color = GlassBorderLight,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(backgroundColor)
    .border(borderWidth, borderColor, shape)

fun Modifier.glassCardGradient(
    shape: Shape = RoundedCornerShape(20.dp),
    brush: Brush = Brush.linearGradient(
        colors = listOf(Color(0x3038BDF8), Color(0x188B5CF6), Color(0x180F172A))
    ),
    borderColor: Color = GlassBorderLight,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(brush)
    .border(borderWidth, borderColor, shape)

fun Modifier.pulsingGlow(
    glowColor: Color = NeonCyan,
    minAlpha: Float = 0.3f,
    maxAlpha: Float = 0.8f
): Modifier = this.drawBehind {
    val radius = size.maxDimension / 1.7f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(glowColor.copy(alpha = maxAlpha), Color.Transparent),
            center = center,
            radius = radius
        ),
        radius = radius
    )
}
