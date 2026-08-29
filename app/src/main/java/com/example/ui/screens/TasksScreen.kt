package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskType
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.TakaFlowViewModel

@Composable
fun TasksScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val taskHistory by viewModel.taskHistory.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .testTag("tasks_screen_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            // Header
            Column {
                Text(
                    text = "আর্নিং ও টাস্ক সেন্টার 🎯",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "প্রতিদিনের টাস্ক পূরণ করে আনলিমিটেড পয়েন্ট অর্জন করুন",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }
        }

        // 1. REFERRAL MEGA CARD (রেফারেল সিস্টেম)
        item {
            GlassGradientCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                gradientColors = listOf(
                    Color(0x40F59E0B),
                    Color(0x30EC4899),
                    Color(0x200F172A)
                ),
                borderColor = AmberGold.copy(alpha = 0.7f),
                contentPadding = PaddingValues(18.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👥", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "রেফার অ্যান্ড আর্ন (Mega Bonus)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AmberGoldLight,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "প্রতি সফল রেফারে ৫০০ পয়েন্ট (৳৫) বোনাস!",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Referral Code Box with Copy
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x35000000))
                            .border(1.dp, GlassBorderGold, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "আপনার রেফারেল কোড",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                            Text(
                                text = user?.referralCode ?: "TKF-88420",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AmberGoldLight,
                                    letterSpacing = 1.sp,
                                    fontSize = 17.sp
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Copy Button
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Referral Code", user?.referralCode ?: "TKF-88420")
                                    clipboard.setPrimaryClip(clip)
                                    viewModel.showToast("রেফারেল কোড কপি করা হয়েছে! 📋")
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x30F59E0B))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = AmberGoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Share Intent Button
                            IconButton(
                                onClick = {
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "টাকাফ্লো (TakaFlow) অ্যাপে সহজে কাজ করে প্রতিদিন বিকাশ/নগদে টাকা আয় করুন! সাইন আপে আমার রেফার কোড ব্যবহার করুন: ${user?.referralCode ?: "TKF-88420"} এবং ফ্রি ২০০ পয়েন্ট বোনাস পান!"
                                        )
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "রেফার লিঙ্ক শেয়ার করুন")
                                    context.startActivity(shareIntent)
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x3006B6D4))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = NeonCyanGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulate Demo Referral Button for Testing
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "+ টেস্ট রেফারেল সিমুলেট (+৫০০ পয়েন্ট)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonEmeraldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2010B981))
                                .clickable { viewModel.simulateReferralShare() }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 2. DAILY BONUS TASK
        item {
            TaskItemCard(
                title = "ডেইলি বোনাস ক্লেইম",
                subtitle = "প্রতিদিন লগইন করে ৫০-১০০ পয়েন্ট ফ্রি সংগ্রহ করুন",
                emoji = "🎁",
                badgeText = "দিন ${user?.dailyStreak ?: 1}",
                actionButtonText = "ক্লেইম করুন",
                gradientColors = listOf(Color(0x3510B981), Color(0x150F172A)),
                borderColor = NeonEmerald.copy(alpha = 0.6f),
                onClick = { viewModel.claimDailyBonus() }
            )
        }

        // 3. LUCKY SPIN WHEEL
        item {
            TaskItemCard(
                title = "লাকি স্পিন হুইল",
                subtitle = "চাকা ঘুরিয়ে ১০ থেকে ৫০০ পয়েন্ট পর্যন্ত জিতুন",
                emoji = "🎡",
                badgeText = "বাকি: ${user?.spinsAvailableToday ?: 5} বার",
                actionButtonText = "স্পিন খেলুন",
                gradientColors = listOf(Color(0x3506B6D4), Color(0x150F172A)),
                borderColor = NeonCyan.copy(alpha = 0.6f),
                onClick = { viewModel.openSpinWheel() }
            )
        }

        // 4. VIDEO WATCH TASK
        item {
            TaskItemCard(
                title = "ভিডিও ওয়াচ টাস্ক",
                subtitle = "ছোট প্রোমোশনাল ভিডিও দেখুন ও ৩০ পয়েন্ট পান",
                emoji = "🎬",
                badgeText = "বাকি: ${user?.videosAvailableToday ?: 8} টি",
                actionButtonText = "ভিডিও দেখুন",
                gradientColors = listOf(Color(0x35EC4899), Color(0x150F172A)),
                borderColor = CoralRose.copy(alpha = 0.6f),
                onClick = { viewModel.openVideoWatch() }
            )
        }

        // 5. MATH & GK QUIZ TASK
        item {
            TaskItemCard(
                title = "ম্যাথ ও জিকে কুইজ",
                subtitle = "বুদ্ধিদীপ্ত প্রশ্নের সঠিক উত্তরে প্রতিটিতে ২০ পয়েন্ট",
                emoji = "🧠",
                badgeText = "বাকি: ${user?.quizzesAvailableToday ?: 10} টি",
                actionButtonText = "কুইজ খেলুন",
                gradientColors = listOf(Color(0x358B5CF6), Color(0x150F172A)),
                borderColor = ElectricViolet.copy(alpha = 0.6f),
                onClick = { viewModel.openQuiz() }
            )
        }

        // 6. GOLDEN SCRATCH CARD
        item {
            TaskItemCard(
                title = "গোল্ডেন স্ক্র্যাচ কার্ড",
                subtitle = "কার্ড ঘষে তাৎক্ষণিক ১৫ থেকে ৩০০ পয়েন্ট রিভিল করুন",
                emoji = "✨",
                badgeText = "বাকি: ${user?.scratchCardsAvailableToday ?: 5} টি",
                actionButtonText = "স্ক্র্যাচ করুন",
                gradientColors = listOf(Color(0x35F59E0B), Color(0x150F172A)),
                borderColor = AmberGold.copy(alpha = 0.6f),
                onClick = { viewModel.openScratchCard() }
            )
        }

        // Task History Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "সাম্প্রতিক অর্জিত পয়েন্ট হিস্ট্রি 📜",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            )
        }

        items(taskHistory) { item ->
            GlassBox(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 16.dp,
                backgroundColor = Color(0x15FFFFFF),
                borderColor = GlassBorderLight,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = item.taskType.icon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = item.timestamp,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Text(
                        text = "+${item.points} 🪙",
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
}

@Composable
private fun TaskItemCard(
    title: String,
    subtitle: String,
    emoji: String,
    badgeText: String,
    actionButtonText: String,
    gradientColors: List<Color>,
    borderColor: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.linearGradient(gradientColors))
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0x25000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonCyanGlow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x3038BDF8))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x30FFFFFF))
                    .border(1.dp, Color(0x60FFFFFF), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = actionButtonText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
