package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WithdrawalMethod
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.TakaFlowViewModel

@Composable
fun WalletScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    var selectedMethod by remember { mutableStateOf(WithdrawalMethod.BKASH) }
    var selectedAmountTaka by remember { mutableStateOf(50) }
    var customAmountText by remember { mutableStateOf("50") }
    var targetPhone by remember { mutableStateOf(user?.phoneOrEmail?.filter { it.isDigit() } ?: "01712345678") }
    var selectedAccountType by remember { mutableStateOf("Personal (পার্সোনাল)") }

    val pointsNeeded = (customAmountText.toIntOrNull() ?: 0) * 100
    val userPoints = user?.points ?: 0
    val hasSufficientPoints = userPoints >= pointsNeeded && pointsNeeded > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp)
            .testTag("wallet_screen_content")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Header
        Text(
            text = "উইথড্রল ও ওয়ালেট ম্যানেজমেন্ট 💳",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 20.sp
            )
        )
        Text(
            text = "বিকাশ, নগদ ও রকেটের মাধ্যমে পয়েন্ট রিডিম করে টাকা তুলুন",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current Balance Summary Glass Card
        GlassGradientCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 22.dp,
            gradientColors = listOf(
                Color(0x3506B6D4),
                Color(0x258B5CF6),
                Color(0x180F172A)
            ),
            borderColor = GlassBorderHighlight,
            contentPadding = PaddingValues(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "আপনার রিডিমেবল ব্যালেন্স",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "৳ ${String.format("%.2f", user?.takaEquivalent ?: 0.0)}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonEmeraldLight,
                            fontSize = 28.sp
                        )
                    )
                    Text(
                        text = "মোট পয়েন্ট: $userPoints টি",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NeonCyanGlow,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }

                // Minimum payout badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2510B981))
                        .border(1.dp, NeonEmerald.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "মিনিমাম: ৳৫০",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonEmeraldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. SELECT PAYMENT METHOD
        Text(
            text = "পেমেন্ট মাধ্যম বেছে নিন 🏦",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WithdrawalMethod.values().forEach { method ->
                val isSelected = selectedMethod == method
                val brandColor = Color(method.colorHex)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) brandColor.copy(alpha = 0.25f)
                            else Color(0x18FFFFFF)
                        )
                        .border(
                            1.5.dp,
                            if (isSelected) brandColor else GlassBorderLight,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedMethod = method }
                        .padding(vertical = 14.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (method) {
                                WithdrawalMethod.BKASH -> "💖"
                                WithdrawalMethod.NAGAD -> "🧡"
                                WithdrawalMethod.ROCKET -> "💜"
                            },
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = method.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "মিনিমাম ৳${method.minTaka}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) brandColor else TextMuted,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. AMOUNT SELECTOR PRESETS
        Text(
            text = "উইথড্রল টাকার পরিমাণ ৳",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(50, 100, 200, 500).forEach { amt ->
                val isSelected = customAmountText == amt.toString()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0x3506B6D4) else Color(0x18FFFFFF))
                        .border(
                            1.dp,
                            if (isSelected) NeonCyan else GlassBorderLight,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            selectedAmountTaka = amt
                            customAmountText = amt.toString()
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳$amt",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) NeonCyanGlow else TextPrimary,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Amount Input
        GlassTextField(
            value = customAmountText,
            onValueChange = { input ->
                if (input.all { it.isDigit() } && input.length <= 5) {
                    customAmountText = input
                }
            },
            placeholder = "কাস্টম টাকার পরিমাণ লিখুন",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Text(
                    text = "৳",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonEmeraldLight
                )
            },
            trailingIcon = {
                Text(
                    text = "= $pointsNeeded পয়েন্ট",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (hasSufficientPoints) NeonEmeraldLight else CoralRose,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            },
            testTag = "withdrawal_amount_field"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. TARGET PHONE NUMBER INPUT
        Text(
            text = "${selectedMethod.titleBn} একাউন্ট নম্বর",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassTextField(
            value = targetPhone,
            onValueChange = { targetPhone = it },
            placeholder = "১১ ডিজিটের নম্বর লিখুন (উদা: 017xxxxxxxx)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PhoneIphone,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            },
            testTag = "withdrawal_phone_field"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. ACCOUNT TYPE (Personal / Agent)
        Text(
            text = "একাউন্ট টাইপ",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Personal (পার্সোনাল)", "Agent (এজেন্ট)").forEach { type ->
                val isSelected = selectedAccountType == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0x358B5CF6) else Color(0x18FFFFFF))
                        .border(
                            1.dp,
                            if (isSelected) ElectricViolet else GlassBorderLight,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedAccountType = type }
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // SUBMIT WITHDRAWAL BUTTON
        GlassPrimaryButton(
            text = "উইথড্র রিকোয়েস্ট সাবমিট করুন (৳${customAmountText})",
            onClick = {
                val amount = customAmountText.toIntOrNull() ?: 0
                viewModel.submitWithdrawal(
                    method = selectedMethod,
                    amountTaka = amount,
                    targetNumber = targetPhone,
                    accountType = selectedAccountType
                ) {}
            },
            enabled = hasSufficientPoints && targetPhone.length >= 11,
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Send,
            gradientColors = listOf(Color(selectedMethod.colorHex), ElectricViolet),
            testTag = "submit_withdrawal_button"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Policy Note Card
        GlassBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp,
            backgroundColor = Color(0x20FFFFFF),
            borderColor = GlassBorderLight,
            contentPadding = PaddingValues(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonCyanGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "পেমেন্ট নিয়মাবলী ও নিরাপত্তা শর্ত",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• উইথড্র সাবমিটের ১-২৪ ঘণ্টার মধ্যে একাউন্টে টাকা যুক্ত হবে।\n• ভুল ফোন নম্বর দিলে পেমেন্ট ব্যর্থ হতে পারে।\n• 'হিস্ট্রি' পেইজে আপনার পেমেন্টের লাইভ স্ট্যাটাস ও TrxID চেক করতে পারেন।",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
