package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.TakaFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    // Dialog state collectors
    val showSpinWheel by viewModel.showSpinWheel.collectAsState()
    val showQuiz by viewModel.showQuiz.collectAsState()
    val showScratchCard by viewModel.showScratchCard.collectAsState()
    val showVideoWatch by viewModel.showVideoWatch.collectAsState()
    val showTelegramModal by viewModel.showTelegramModal.collectAsState()
    val showAdminSheet by viewModel.showAdminSheet.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    TakaFlowBackground(modifier = modifier) {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    GlassBox(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        cornerRadius = 14.dp,
                        backgroundColor = Color(0xF00F172A),
                        borderColor = NeonCyan,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            },
            topBar = {
                // Top App Bar with Glassmorphism
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    GlassBox(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 20.dp,
                        backgroundColor = Color(0x22FFFFFF),
                        borderColor = GlassBorderLight,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Brand
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.selectTab(MainTab.HOME) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(NeonCyan, ElectricViolet))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "৳",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "টাকাফ্লো",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            fontSize = 16.sp
                                        )
                                    )
                                    Text(
                                        text = "TakaFlow",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NeonCyanGlow,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            // Right Action Icons: Points Pill + Admin Toggle + Logout
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Balance Chip
                                PointsBadge(
                                    points = user?.points ?: 0,
                                    modifier = Modifier.clickable { viewModel.selectTab(MainTab.WALLET) }
                                )

                                // Admin Toggle Button
                                IconButton(
                                    onClick = { viewModel.openAdminSheet() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isAdminMode) Color(0x40F43F5E) else Color(0x20FFFFFF))
                                        .border(1.dp, if (isAdminMode) CoralRose else GlassBorderLight, CircleShape)
                                        .testTag("top_admin_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = "Admin Panel",
                                        tint = if (isAdminMode) CoralRose else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Logout
                                IconButton(
                                    onClick = { viewModel.logout() },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Logout,
                                        contentDescription = "Logout",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                // Bottom Glassmorphic Navigation Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    GlassBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("bottom_nav_bar"),
                        cornerRadius = 24.dp,
                        backgroundColor = Color(0xD00A0F1E),
                        borderColor = GlassBorderHighlight,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MainTab.values().forEach { tab ->
                                val isSelected = currentTab == tab

                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isSelected) Color(0x3038BDF8) else Color.Transparent
                                        )
                                        .clickable { viewModel.selectTab(tab) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("nav_tab_${tab.name.lowercase()}"),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = when (tab) {
                                            MainTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                                            MainTab.TASKS -> if (isSelected) Icons.Filled.Stars else Icons.Outlined.Stars
                                            MainTab.WALLET -> if (isSelected) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet
                                            MainTab.HISTORY -> if (isSelected) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong
                                            MainTab.AI_SUPPORT -> if (isSelected) Icons.Filled.SmartToy else Icons.Outlined.SmartToy
                                        },
                                        contentDescription = tab.titleBn,
                                        tint = if (isSelected) NeonCyanGlow else TextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tab.titleBn,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else TextMuted,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab Content Switching
                when (currentTab) {
                    MainTab.HOME -> HomeScreen(viewModel = viewModel)
                    MainTab.TASKS -> TasksScreen(viewModel = viewModel)
                    MainTab.WALLET -> WalletScreen(viewModel = viewModel)
                    MainTab.HISTORY -> HistoryScreen(viewModel = viewModel)
                    MainTab.AI_SUPPORT -> AiSupportScreen(viewModel = viewModel)
                }

                // FLOATING TELEGRAM PULSING BUTTON (Bottom-Right Corner)
                TelegramFloatingPulsingButton(
                    onClick = { viewModel.openTelegramModal() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 12.dp)
                )
            }
        }

        // ==========================================
        // DIALOGS & POPUPS
        // ==========================================

        if (showSpinWheel) {
            SpinWheelDialog(
                spinsLeft = user?.spinsAvailableToday ?: 5,
                onDismiss = { viewModel.closeSpinWheel() },
                onSpinComplete = { won -> viewModel.onSpinCompleted(won) }
            )
        }

        if (showQuiz) {
            QuizDialog(
                quizzesLeft = user?.quizzesAvailableToday ?: 10,
                onDismiss = { viewModel.closeQuiz() },
                onQuizSuccess = { viewModel.onQuizCorrect() }
            )
        }

        if (showScratchCard) {
            ScratchCardDialog(
                scratchCardsLeft = user?.scratchCardsAvailableToday ?: 5,
                onDismiss = { viewModel.closeScratchCard() },
                onScratchComplete = { pts -> viewModel.onScratchCompleted(pts) }
            )
        }

        if (showVideoWatch) {
            VideoWatchDialog(
                videosLeft = user?.videosAvailableToday ?: 8,
                onDismiss = { viewModel.closeVideoWatch() },
                onVideoComplete = { viewModel.onVideoCompleted() }
            )
        }

        if (showTelegramModal) {
            TelegramCommunityModal(
                onDismiss = { viewModel.closeTelegramModal() }
            )
        }

        // Admin BottomSheet / Dialog
        if (showAdminSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.closeAdminSheet() },
                containerColor = DarkBgStart,
                scrimColor = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                AdminScreen(
                    viewModel = viewModel,
                    onClose = { viewModel.closeAdminSheet() }
                )
            }
        }
    }
}
