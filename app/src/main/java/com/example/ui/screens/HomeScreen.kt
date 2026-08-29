package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TakaFlowRepository
import com.example.model.TaskType
import com.example.model.User
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.TakaFlowViewModel

@Composable
fun HomeScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()
    val announcements = remember { TakaFlowRepository.announcements }
    val topEarners = remember { TakaFlowRepository.topEarners }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp)
            .testTag("home_screen_content")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Greeting & Admin Mode Chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "শুভ দিন, ${user?.name?.substringBefore(" ") ?: "ইউজার"} 👋",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = "টাকাফ্লো ডিজিটাল ড্যাশবোর্ড",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }

            // Daily Streak Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x35F59E0B))
                    .border(1.dp, AmberGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔥", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${user?.dailyStreak ?: 1} দিন স্ট্রিক",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AmberGoldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // MAIN BALANCE GLASS CARD (মোট পয়েন্ট, মোট উইথড্র, রেফারেল)
        GlassGradientCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("main_balance_card"),
            cornerRadius = 24.dp,
            gradientColors = listOf(
                Color(0x4006B6D4),
                Color(0x308B5CF6),
                Color(0x250F172A)
            ),
            borderColor = GlassBorderHighlight,
            contentPadding = PaddingValues(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "বর্তমান মোট পয়েন্ট",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${user?.points ?: 0}",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary,
                                    fontSize = 34.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "পয়েন্ট",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = NeonCyanGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    // Taka Equivalent Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x3510B981))
                            .border(1.dp, NeonEmerald.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "সমপরিমাণ টাকা",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                            Text(
                                text = "৳ ${String.format("%.2f", user?.takaEquivalent ?: 0.0)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonEmeraldLight,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Divider(color = Color(0x25FFFFFF), thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Sub Stats Row (মোট উইথড্র & রেফারেল সংখ্যা)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Total Withdrawn
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x25EC4899)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💳", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "মোট উইথড্র",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "৳ ${user?.totalWithdrawnTaka ?: 0}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }

                    // Total Referrals
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x25F59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👥", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "রেফারেল সংখ্যা",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "${user?.referralCount ?: 0} জন",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldLight,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Conversion Note Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x2038BDF8))
                .border(0.8.dp, Color(0x3538BDF8), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💡", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "কনভার্সন রেট: ১০০০ পয়েন্ট = ১০ টাকা | বিকাশ/নগদে সর্বনিম্ন ৫০ টাকা",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = NeonCyanGlow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // QUICK TASK SHORTCUTS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "আর্নিং টাস্কসমূহ ⚡",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            )
            Text(
                text = "সবগুলো দেখুন →",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = NeonCyanGlow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                modifier = Modifier.clickable {
                    viewModel.selectTab(MainTab.TASKS)
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of 4 quick tasks
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Daily Bonus
            QuickTaskCard(
                title = "ডেইলি বোনাস",
                reward = "+৫০-১০০ পয়েন্ট",
                emoji = "🎁",
                gradient = listOf(Color(0x3510B981), Color(0x150F172A)),
                borderColor = NeonEmerald.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.claimDailyBonus() }
            )

            // Spin Wheel
            QuickTaskCard(
                title = "লাকি স্পিন",
                reward = "১০-৫০০ পয়েন্ট",
                emoji = "🎡",
                gradient = listOf(Color(0x3506B6D4), Color(0x150F172A)),
                borderColor = NeonCyan.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openSpinWheel() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Math & GK Quiz
            QuickTaskCard(
                title = "নলেজ কুইজ",
                reward = "+২০ পয়েন্ট",
                emoji = "🧠",
                gradient = listOf(Color(0x358B5CF6), Color(0x150F172A)),
                borderColor = ElectricViolet.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openQuiz() }
            )

            // Golden Scratch Card
            QuickTaskCard(
                title = "স্ক্র্যাচ কার্ড",
                reward = "১৫-৩০০ পয়েন্ট",
                emoji = "✨",
                gradient = listOf(Color(0x35F59E0B), Color(0x150F172A)),
                borderColor = AmberGold.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openScratchCard() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ANNOUNCEMENT BANNER
        announcements.firstOrNull()?.let { ann ->
            GlassBox(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                backgroundColor = Color(0x251E1B4B),
                borderColor = ElectricViolet.copy(alpha = 0.5f),
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "📢", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = ann.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberGoldLight,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ann.text,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TOP EARNERS LEADERBOARD PREVIEW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "সর্বোচ্চ আয়কারী (Leaderboard) 🏆",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            )
            Text(
                text = "এই সপ্তাহ",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topEarners.take(3).forEach { earner ->
                GlassBox(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 16.dp,
                    backgroundColor = Color(0x18FFFFFF),
                    borderColor = if (earner.rank == 1) AmberGold.copy(alpha = 0.6f) else GlassBorderLight,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Rank Medal
                            Text(
                                text = when (earner.rank) {
                                    1 -> "🥇"
                                    2 -> "🥈"
                                    3 -> "🥉"
                                    else -> "#${earner.rank}"
                                },
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = earner.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "রেফারেল: ${earner.referralCount} জন",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "৳ ${earner.totalEarnedTaka}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonEmeraldLight,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun QuickTaskCard(
    title: String,
    reward: String,
    emoji: String,
    gradient: List<Color>,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .height(104.dp)
            .clip(shape)
            .background(Brush.linearGradient(gradient))
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(text = emoji, fontSize = 24.sp)
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = reward,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = NeonCyanGlow,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
