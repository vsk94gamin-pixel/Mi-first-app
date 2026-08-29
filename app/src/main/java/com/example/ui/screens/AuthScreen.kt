package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.TakaFlowViewModel

@Composable
fun AuthScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    var isSignUpTab by remember { mutableStateOf(false) }

    // Form states
    var name by remember { mutableStateOf("") }
    var phoneOrEmail by remember { mutableStateOf("01712345678") }
    var password by remember { mutableStateOf("password123") }
    var referralCode by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    TakaFlowBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo & Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(NeonCyan, ElectricViolet))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "টাকাফ্লো (TakaFlow)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "ডিজিটাল আর্নিং প্ল্যাটফর্ম",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NeonCyanGlow,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tab Selector Glass Container
            GlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_tab_selector"),
                cornerRadius = 18.dp,
                backgroundColor = Color(0x20FFFFFF),
                contentPadding = PaddingValues(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Login Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (!isSignUpTab) Modifier.background(Brush.horizontalGradient(listOf(NeonCyan, ElectricViolet)))
                                else Modifier.background(Color.Transparent)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple()
                            ) { isSignUpTab = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "লগইন (Login)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (!isSignUpTab) FontWeight.Bold else FontWeight.Normal,
                                color = if (!isSignUpTab) Color.White else TextSecondary,
                                fontSize = 14.sp
                            )
                        )
                    }

                    // Sign Up Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (isSignUpTab) Modifier.background(Brush.horizontalGradient(listOf(NeonCyan, ElectricViolet)))
                                else Modifier.background(Color.Transparent)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple()
                            ) { isSignUpTab = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "সাইন আপ (Sign Up)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isSignUpTab) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSignUpTab) Color.White else TextSecondary,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Glass Form Card
            GlassGradientCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                contentPadding = PaddingValues(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isSignUpTab) "নতুন একাউন্ট খুলুন 🚀" else "আপনার একাউন্টে লগইন করুন 🔑",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 17.sp
                        )
                    )

                    if (isSignUpTab) {
                        // Referral bonus banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x2810B981))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🎁", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "নতুন একাউন্টে ৫০০ পয়েন্ট এবং রেফার কোডে অতিরিক্ত ২০০ পয়েন্ট বোনাস!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonEmeraldLight,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        }

                        // Name Field
                        Column {
                            Text(
                                text = "আপনার পুরো নাম",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            GlassTextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = "উদা: তানভীর আহমেদ",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                testTag = "signup_name_field"
                            )
                        }
                    }

                    // Phone or Email Field
                    Column {
                        Text(
                            text = "ফোন নম্বর অথবা ইমেইল",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        GlassTextField(
                            value = phoneOrEmail,
                            onValueChange = { phoneOrEmail = it },
                            placeholder = "উদা: 017xxxxxxxx অথবা example@mail.com",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.PhoneIphone,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            testTag = "auth_phone_field"
                        )
                    }

                    // Password Field
                    Column {
                        Text(
                            text = "গোপন পাসওয়ার্ড",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        GlassTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "পাসওয়ার্ড লিখুন",
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { showPassword = !showPassword },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            },
                            testTag = "auth_password_field"
                        )
                    }

                    if (isSignUpTab) {
                        // Referral Code Field (Optional)
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "রেফারেল কোড (ঐচ্ছিক)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+২০০ পয়েন্ট বোনাস",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = AmberGoldLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            GlassTextField(
                                value = referralCode,
                                onValueChange = { referralCode = it },
                                placeholder = "উদা: TKF-88420",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.GroupAdd,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                testTag = "signup_referral_field"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Submit Button
                    GlassPrimaryButton(
                        text = if (isSignUpTab) "একাউন্ট তৈরি করুন (+৫০০ পয়েন্ট)" else "লগইন করুন",
                        onClick = {
                            if (isSignUpTab) {
                                viewModel.signUp(name, phoneOrEmail, password, referralCode) {}
                            } else {
                                viewModel.login(phoneOrEmail, password) {}
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = if (isSignUpTab) Icons.Default.PersonAdd else Icons.Default.Login,
                        testTag = "auth_submit_button"
                    )

                    // Quick Demo Credentials Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "দ্রুত টেস্ট করতে:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "ডেমো ইউজার",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonCyanGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier
                                    .clickable {
                                        phoneOrEmail = "01712345678"
                                        password = "password123"
                                        isSignUpTab = false
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x2038BDF8))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Text(
                                text = "ডেমো রেফার",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AmberGoldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier
                                    .clickable {
                                        name = "মেহেদী হাসান"
                                        phoneOrEmail = "01999887766"
                                        password = "demoPassword"
                                        referralCode = "TKF-88420"
                                        isSignUpTab = true
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x20F59E0B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Security note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "১০০% নিরাপদ ও ইনস্ট্যান্ট এমএফএস পেমেন্ট সুবিধা",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
